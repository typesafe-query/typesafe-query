package com.github.typesafe_query.tools;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

import com.github.typesafe_query.tools.util.CodeUtils;
import com.github.typesafe_query.tools.util.Field;
import com.github.typesafe_query.tools.util.JavaClass;
import com.github.typesafe_query.tools.util.Method;
import com.github.typesafe_query.tools.util.Qualifiers;

import javax.tools.JavaFileObject;

@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes({
	"com.github.typesafe_query.annotation.Table"
})
public class MetaProcessor extends AbstractProcessor {
	/** _ */
	public static final String META_CLASS_SUFFIX = "_";
	/** \"Model Meta Generator\" */
	public static final String GENERATED_VALUE = "\"Meta Generator\"";

	/** javax.annotation.Generated */
	public static final String ANOT_GENERATED = "javax.annotation.Generated";

	public static final String ANOT_ID = "com.github.typesafe_query.annotation.Id";
	public static final String ANOT_EMBEDDED_ID = "com.github.typesafe_query.annotation.EmbeddedId";
	public static final String ANOT_EMBEDDED = "com.github.typesafe_query.annotation.Embedded";
	public static final String ANOT_TABLE = "com.github.typesafe_query.annotation.Table";
	public static final String ANOT_COLUMN = "com.github.typesafe_query.annotation.Column";
	public static final String ANOT_TRANSIENT = "com.github.typesafe_query.annotation.Transient";

	public static final String PACKAGE_NAME = "com.github.typesafe_query";
	
	public static final JavaClass GENERATED_ANNOTATION = new JavaClass(ANOT_GENERATED);

	public static final JavaClass IDBTABLE_CLASS = new JavaClass(PACKAGE_NAME + ".meta.IDBTable");

	private JavaClass metaClass;

	private String idClass;
	
	private List<String> targetFields;

	@Override
	public boolean process(Set<? extends TypeElement> annotations,
			RoundEnvironment roundEnv) {
		Filer f = processingEnv.getFiler();
		Messager messager = processingEnv.getMessager();

		for (TypeElement annotation : annotations) {
			Set<TypeElement> classes = ElementFilter.typesIn(roundEnv.getElementsAnnotatedWith(annotation));
			for (TypeElement te : classes) {

				try {

					String metaClassName = te.getQualifiedName().toString() + META_CLASS_SUFFIX;
					String metaClassSimpleName = te.getSimpleName().toString() + META_CLASS_SUFFIX;

					idClass = null;
					targetFields = new ArrayList<String>();
					metaClass = new JavaClass(metaClassName);
					metaClass.setFinal(true);
					metaClass.addAnnotaion(GENERATED_ANNOTATION, GENERATED_VALUE);
					Method m = new Method(metaClassSimpleName);
					m.setConstractor(true);
					m.setQualifier(Qualifiers.PRIVATE);
					metaClass.addMethod(m);

					processType(te);

					//ファイルを作る
					JavaFileObject jfo = f.createSourceFile(metaClassName);
					PrintWriter writer = new PrintWriter(jfo.openOutputStream());
					//ファイルに書き込む
					writer.write(metaClass.toJavaCodeAll());
					writer.close();
				} catch (Exception e) {
					StringWriter sw = new StringWriter();
					PrintWriter pw = new PrintWriter(sw);
					e.printStackTrace(pw);

					messager.printMessage(Kind.ERROR, "メタクラスを作成できませんでした。\n" + sw.toString(),te);
				}
			}
		}
		return true;
	}

	private boolean isId(VariableElement ve){
		boolean embedded = Utils.hasAnnotation(ve, ANOT_EMBEDDED_ID);
		boolean id = Utils.hasAnnotation(ve, ANOT_ID);
		return id || embedded;
	}

	private void processType(TypeElement te){
		//クラスから@Tableを取得してメタクラス、IDBTableを定義する。
		//デフォルトスネークケース
		String tableName = camelToSnake(te.getSimpleName().toString());
		AnnotationMirror tAnot = Utils.getAnnotation(te, ANOT_TABLE);
		if(tAnot !=null){
			String tn = Utils.getAnnotationPropertyValue(tAnot, "name");
			if(tn != null && !tn.isEmpty()){
				tableName = tn;
			}
		}

		metaClass.addImport(new JavaClass(PACKAGE_NAME + ".meta.impl.DBTableImpl"));
		
		Field table = new Field("TABLE");
		table.setQualifier(Qualifiers.PUBLIC);
		table.setType(IDBTABLE_CLASS);
		table.setFinal(true);
		table.setStatical(true);
		table.setInitializeString(String.format("new DBTableImpl(\"%s\")",tableName));

		metaClass.addField(table);

		//privateフィールドを取得して、IDBColumnを定義していく。
		List<VariableElement> fields = ElementFilter.fieldsIn(te.getEnclosedElements());
		for(VariableElement ve : fields){
			//IDを確認
			if(isId(ve)){
				TypeElement typeElement = null;
				DeclaredType fType = null;
				if (ve.asType() instanceof DeclaredType) {
					// オブジェクト型の処理
					fType = (DeclaredType)ve.asType();
					typeElement = (TypeElement)fType.asElement();
				} else if (ve.asType() instanceof PrimitiveType)  {
					// その他はプリミティブ型
					PrimitiveType pType = (PrimitiveType)ve.asType();
					Types type = processingEnv.getTypeUtils();
					typeElement = type.boxedClass(pType);
				} else {
					//その他は配列のため、メタデータにカラムは作成しない
					continue;
				}
				idClass = typeElement.getQualifiedName().toString();
			}

			writeIDBColumn(ve,null);
		}
		if(idClass == null){
			throw new RuntimeException("Entityクラスに有効なIDが存在しません。アノテーションを確認してください。");
		}
		
		//_FIELDS,_DESCを定義する
		
		int count = 0;
		StringBuilder sb = new StringBuilder();
		for(String n : targetFields){
			if(count != 0){
				sb.append(",");
			}
			sb.append("\"").append(n).append("\"");
			
			count++;
		}
		
		JavaClass _fields = new JavaClass("java.util.List");
		_fields.setGenericTypes(new JavaClass[]{new JavaClass("String")});

		Field _fld = new Field("_FIELDS");
		_fld.setQualifier(Qualifiers.PRIVATE);
		_fld.setStatical(true);
		_fld.setFinal(true);
		_fld.setType(_fields);
		_fld.setInitializeString("Arrays.asList(" + sb.toString() + ")");

		metaClass.addImport(new JavaClass("java.util.Arrays"));
		metaClass.addField(_fld);

		JavaClass _desc = new JavaClass(PACKAGE_NAME + ".ModelDescription");
		
		Field _dsc = new Field("_DESC");
		_dsc.setQualifier(Qualifiers.PRIVATE);
		_dsc.setStatical(true);
		_dsc.setFinal(true);
		_dsc.setType(_desc);
		_dsc.setInitializeString("new ModelDescription("+ te.getSimpleName().toString() +".class, _FIELDS)");

		metaClass.addField(_dsc);
		
		//ModelHandler,Finder,Bulkを定義する。
		JavaClass modelHandler = new JavaClass(PACKAGE_NAME + ".ModelHandler");
		modelHandler.setGenericTypes(new JavaClass[]{new JavaClass(te.getSimpleName().toString())});

		Field mf = new Field("model");
		mf.setQualifier(Qualifiers.PRIVATE);
		mf.setStatical(true);
		mf.setFinal(true);
		mf.setType(modelHandler);
		mf.setInitializeString("new ModelHandler<"+te.getSimpleName().toString()+">(" + te.getSimpleName().toString() +".class,TABLE,_DESC)");

		metaClass.addField(mf);
		
		Method mm = new Method("model");
		mm.setQualifier(Qualifiers.PUBLIC);
		mm.setStatical(true);
		mm.setReturnType(modelHandler);
		mm.addBodyLine("return model;");
		
		metaClass.addMethod(mm);
		
		JavaClass reusableModelHandler = new JavaClass(PACKAGE_NAME + ".ReusableModelHandler");
		reusableModelHandler.setGenericTypes(new JavaClass[]{new JavaClass(te.getSimpleName().toString())});

		Method mmr = new Method("modelForReuse");
		mmr.setQualifier(Qualifiers.PUBLIC);
		mmr.setStatical(true);
		mmr.setReturnType(reusableModelHandler);
		mmr.addBodyLine("return new ReusableModelHandler<>"+"(" + te.getSimpleName().toString() +".class,TABLE,_DESC);");
		
		metaClass.addMethod(mmr);

		JavaClass finder = new JavaClass(PACKAGE_NAME + ".Finder");
		finder.setGenericTypes(new JavaClass[]{new JavaClass(idClass),new JavaClass(te.getSimpleName().toString())});

		Field ff = new Field("find");
		ff.setQualifier(Qualifiers.PRIVATE);
		ff.setStatical(true);
		ff.setFinal(true);
		ff.setType(finder);
		ff.setInitializeString("new DefaultFinder<"+idClass+","+te.getSimpleName().toString()+">(" + te.getSimpleName().toString() +".class,TABLE,_DESC)");

		metaClass.addImport(new JavaClass(PACKAGE_NAME + ".DefaultFinder"));
		
		metaClass.addField(ff);
		
		Method fm = new Method("find");
		fm.setQualifier(Qualifiers.PUBLIC);
		fm.setStatical(true);
		fm.setReturnType(finder);
		fm.addBodyLine("return find;");
		
		metaClass.addMethod(fm);
		
		JavaClass bulk = new JavaClass(PACKAGE_NAME + ".Bulk");
		
		Field bf = new Field("bulk");
		bf.setQualifier(Qualifiers.PRIVATE);
		bf.setStatical(true);
		bf.setFinal(true);
		bf.setType(bulk);
		bf.setInitializeString("new DefaultBulk(TABLE)");
		
		metaClass.addImport(new JavaClass(PACKAGE_NAME + ".DefaultBulk"));
		metaClass.addField(bf);
		
		Method bm = new Method("bulk");
		bm.setQualifier(Qualifiers.PUBLIC);
		bm.setStatical(true);
		bm.setReturnType(bulk);
		bm.addBodyLine("return bulk;");

		metaClass.addMethod(bm);

	}

	private void writeIDBColumn(VariableElement ve,String colName){

		Set<Modifier> modifiers = ve.getModifiers();
		//余計な修飾子がついてたら無視
		if(modifiers.contains(Modifier.STATIC)
				|| modifiers.contains(Modifier.TRANSIENT)
				|| modifiers.contains(Modifier.NATIVE)
				|| modifiers.contains(Modifier.VOLATILE)
				){
			return;
		}

		//@Transientは無視
		if(Utils.hasAnnotation(ve, ANOT_TRANSIENT)){
			return;
		}

		TypeElement typeElement = null;
		DeclaredType fType = null;
		if (ve.asType() instanceof DeclaredType) {
			fType = (DeclaredType)ve.asType();
			typeElement = (TypeElement)fType.asElement();
		} else if (ve.asType() instanceof PrimitiveType) {
			// その他はプリミティブ型
			PrimitiveType pType = (PrimitiveType)ve.asType();
			Types type = processingEnv.getTypeUtils();
			typeElement = type.boxedClass(pType);
		} else {
			//型が不明の場合はここで処理を終了する
			return;
		}

		Field f = new Field(CodeUtils.to_(ve.getSimpleName().toString()).toUpperCase());
		f.setQualifier(Qualifiers.PUBLIC);
		f.setStatical(true);
		f.setFinal(true);

		//colName補正
//		if(colName != null){
//			colName = colName + "." + ve.getSimpleName().toString();
//		}else{
//			AnnotationMirror tAnot = Utils.getAnnotation(ve, JPA_ANOT_COLUMN);
//			if(tAnot !=null){
//				colName = Utils.getAnnotationPropertyValue(tAnot, "name");
//			}
//			if(colName == null){
//				colName = ve.getSimpleName().toString();
//			}
//		}
		String parentColName = colName;
		colName = null;
		AnnotationMirror tAnot = Utils.getAnnotation(ve, ANOT_COLUMN);
		if(tAnot !=null){
			colName = Utils.getAnnotationPropertyValue(tAnot, "name");
		}
		if(colName == null){
			colName = camelToSnake(ve.getSimpleName().toString());
		}

		//文字、数値、日付
		TypeElement te = typeElement;
		String typeName = te.getQualifiedName().toString();

		boolean embedded = Utils.hasAnnotation(ve, ANOT_EMBEDDED_ID) || Utils.hasAnnotation(ve, ANOT_EMBEDDED);
		
		if(!embedded){
			if(parentColName != null){
				targetFields.add(parentColName + "/" + ve.getSimpleName().toString());
			}else{
				targetFields.add(ve.getSimpleName().toString());
			}
		}
		
		//Optionalの場合分解する
		if("java.util.Optional".equals(typeName)){
			List<? extends TypeMirror> generics = fType.getTypeArguments();
			if(!generics.isEmpty()){
				DeclaredType gtm = (DeclaredType)generics.get(0);
				TypeElement gte = (TypeElement)gtm.asElement();
				typeName = gte.getQualifiedName().toString();
			}else{
				typeName = Object.class.getName();
			}
		}
		
		//型によって定義する
		if(embedded){
			//@EmbeddedIdまたは@Embeddedの場合、分解して定義する。
			List<VariableElement> vars = ElementFilter.fieldsIn(te.getEnclosedElements());
			for(VariableElement nve : vars){
				writeIDBColumn(nve, colName);
			}
		}else if("java.lang.Short".equals(typeName) || "short".equals(typeName)){
			//INumberDBColumn<java.lang.Short>
			JavaClass jc = new JavaClass(PACKAGE_NAME + ".meta.INumberDBColumn");
			jc.setGenericTypes(new JavaClass[]{new JavaClass("Short")});

			f.setType(jc);
			metaClass.addImport(new JavaClass(PACKAGE_NAME + ".meta.impl.NumberDBColumnImpl"));
			f.setInitializeString(String.format("new NumberDBColumnImpl<>(TABLE,\"%s\")",colName));

			metaClass.addField(f);
		}else if("java.lang.Integer".equals(typeName) || "int".equals(typeName)){
			//INumberDBColumn<java.lang.Integer>
			JavaClass jc = new JavaClass(PACKAGE_NAME + ".meta.INumberDBColumn");
			jc.setGenericTypes(new JavaClass[]{new JavaClass("Integer")});

			f.setType(jc);
			metaClass.addImport(new JavaClass(PACKAGE_NAME + ".meta.impl.NumberDBColumnImpl"));
			f.setInitializeString(String.format("new NumberDBColumnImpl<>(TABLE,\"%s\")",colName));

			metaClass.addField(f);
		}else if("java.lang.Long".equals(typeName) || "long".equals(typeName)){
			//INumberDBColumn<java.lang.Long>
			JavaClass jc = new JavaClass(PACKAGE_NAME + ".meta.INumberDBColumn");
			jc.setGenericTypes(new JavaClass[]{new JavaClass("Long")});

			f.setType(jc);
			metaClass.addImport(new JavaClass(PACKAGE_NAME + ".meta.impl.NumberDBColumnImpl"));
			f.setInitializeString(String.format("new NumberDBColumnImpl<>(TABLE,\"%s\")",colName));

			metaClass.addField(f);
		}else if("java.lang.Double".equals(typeName) || "double".equals(typeName)){
			//INumberDBColumn<java.lang.Double>
			JavaClass jc = new JavaClass(PACKAGE_NAME + ".meta.INumberDBColumn");
			jc.setGenericTypes(new JavaClass[]{new JavaClass("Double")});

			f.setType(jc);
			metaClass.addImport(new JavaClass(PACKAGE_NAME + ".meta.impl.NumberDBColumnImpl"));
			f.setInitializeString(String.format("new NumberDBColumnImpl<>(TABLE,\"%s\")",colName));

			metaClass.addField(f);
		}else if("java.lang.Float".equals(typeName) || "float".equals(typeName)){
			//INumberDBColumn<java.lang.Float>
			JavaClass jc = new JavaClass(PACKAGE_NAME + ".meta.INumberDBColumn");
			jc.setGenericTypes(new JavaClass[]{new JavaClass("Float")});

			f.setType(jc);
			metaClass.addImport(new JavaClass(PACKAGE_NAME + ".meta.impl.NumberDBColumnImpl"));
			f.setInitializeString(String.format("new NumberDBColumnImpl<>(TABLE,\"%s\")",colName));

			metaClass.addField(f);
		}else if("java.lang.Boolean".equals(typeName) || "boolean".equals(typeName)){
			//IBooleanDBColumn
			JavaClass jc = new JavaClass(PACKAGE_NAME + ".meta.IBooleanDBColumn");

			f.setType(jc);
			metaClass.addImport(new JavaClass(PACKAGE_NAME + ".meta.impl.BooleanDBColumnImpl"));
			f.setInitializeString(String.format("new BooleanDBColumnImpl(TABLE,\"%s\")",colName));

			metaClass.addField(f);
		}else if("java.math.BigDecimal".equals(typeName)){
			//INumberDBColumn<java.math.BigDecimal>
			JavaClass jc = new JavaClass(PACKAGE_NAME + ".meta.INumberDBColumn");
			jc.setGenericTypes(new JavaClass[]{new JavaClass("BigDecimal")});

			f.setType(jc);
			metaClass.addImport(new JavaClass(PACKAGE_NAME + ".meta.impl.NumberDBColumnImpl"));
			f.setInitializeString(String.format("new NumberDBColumnImpl<>(TABLE,\"%s\")",colName));
			metaClass.addField(f);
		}else if("java.lang.String".equals(typeName)){
			//IStringDBColumn
			JavaClass jc = new JavaClass(PACKAGE_NAME + ".meta.IStringDBColumn");

			f.setType(jc);
			metaClass.addImport(new JavaClass(PACKAGE_NAME + ".meta.impl.StringDBColumnImpl"));
			f.setInitializeString(String.format("new StringDBColumnImpl(TABLE,\"%s\")",colName));

			metaClass.addField(f);
		}else if("java.time.LocalDate".equals(typeName)){
			//IDateDBColumn<java.time.LocalDate>
			JavaClass jc = new JavaClass(PACKAGE_NAME + ".meta.IDateDBColumn");
			jc.setGenericTypes(new JavaClass[]{new JavaClass("java.time.LocalDate")});

			f.setType(jc);
			metaClass.addImport(new JavaClass(PACKAGE_NAME + ".meta.impl.DateDBColumnImpl"));
			f.setInitializeString(String.format("new DateDBColumnImpl<>(TABLE,\"%s\")",colName));
			metaClass.addField(f);
		}else if("java.time.LocalTime".equals(typeName)){
			//IDateDBColumn<java.time.LocalTime>
			JavaClass jc = new JavaClass(PACKAGE_NAME + ".meta.IDateDBColumn");
			jc.setGenericTypes(new JavaClass[]{new JavaClass("java.time.LocalTime")});

			f.setType(jc);
			metaClass.addImport(new JavaClass(PACKAGE_NAME + ".meta.impl.DateDBColumnImpl"));
			f.setInitializeString(String.format("new DateDBColumnImpl<>(TABLE,\"%s\")",colName));
			metaClass.addField(f);
		}else if("java.time.LocalDateTime".equals(typeName)){
			//IDateDBColumn<java.time.LocalDateTime>
			JavaClass jc = new JavaClass(PACKAGE_NAME + ".meta.IDateDBColumn");
			jc.setGenericTypes(new JavaClass[]{new JavaClass("java.time.LocalDateTime")});

			f.setType(jc);
			metaClass.addImport(new JavaClass(PACKAGE_NAME + ".meta.impl.DateDBColumnImpl"));
			f.setInitializeString(String.format("new DateDBColumnImpl<>(TABLE,\"%s\")",colName));
			metaClass.addField(f);
		}else if("java.sql.Date".equals(typeName)){
			//IDateDBColumn<java.sql.Date>
			JavaClass jc = new JavaClass(PACKAGE_NAME + ".meta.IDateDBColumn");
			jc.setGenericTypes(new JavaClass[]{new JavaClass("java.sql.Date")});

			f.setType(jc);
			metaClass.addImport(new JavaClass(PACKAGE_NAME + ".meta.impl.DateDBColumnImpl"));
			f.setInitializeString(String.format("new DateDBColumnImpl<java.sql.Date>(TABLE,\"%s\")",colName));
			metaClass.addField(f);
		}else if("java.sql.Time".equals(typeName)){
			//IDateDBColumn<java.sql.Time>
			JavaClass jc = new JavaClass(PACKAGE_NAME + ".meta.IDateDBColumn");
			jc.setGenericTypes(new JavaClass[]{new JavaClass("java.sql.Time")});

			f.setType(jc);
			metaClass.addImport(new JavaClass(PACKAGE_NAME + ".meta.impl.DateDBColumnImpl"));
			f.setInitializeString(String.format("new DateDBColumnImpl<java.sql.Time>(TABLE,\"%s\")",colName));
			metaClass.addField(f);
		}else if("java.sql.Timestamp".equals(typeName)){
			//IDateDBColumn<java.sql.Timestamp>
			JavaClass jc = new JavaClass(PACKAGE_NAME + ".meta.IDateDBColumn");
			jc.setGenericTypes(new JavaClass[]{new JavaClass("java.sql.Timestamp")});

			f.setType(jc);
			metaClass.addImport(new JavaClass(PACKAGE_NAME + ".meta.impl.DateDBColumnImpl"));
			f.setInitializeString(String.format("new DateDBColumnImpl<java.sql.Timestamp>(TABLE,\"%s\")",colName));
			metaClass.addField(f);
		}else{
			throw new RuntimeException("対応していない型です " + typeName);
		}
	}
	
	private String camelToSnake(String targetStr) {
		String convertedStr = targetStr
				.replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
				.replaceAll("([a-z])([A-Z])", "$1_$2");
		return convertedStr.toLowerCase();
	}
}
