package com.github.typesafe_query.tools.util;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;

import com.github.typesafe_query.tools.util.Method.MethodArgment;

import java.util.Set;
import java.util.TreeSet;

/**
 * Javaのクラスを表すクラスです。
 * @author MOSA Takahiko Satou
 *
 */
public class JavaClass {


        private Qualifiers qualifier = Qualifiers.PUBLIC;
        private boolean isStatical;
        private boolean isFinal;
        private boolean isInterface;
        private boolean isInnerClass;
        private boolean isAbstract;
        private String fullyClassName;
        private String simpleName;
        private String javadocComment;
        private Set<Field> fields;
        private Set<Method> methods;
        private JavaClass superClass;
        private JavaClass parentClass;
        private Set<JavaClass> interfaces;
        private Set<JavaClass> innerClasses;
        private Set<JavaClass> imports;
        private Set<String> staticImports;
        private JavaClass[] genericTypes;
        private Map<String, Integer> fieldMap;
        private Map<JavaClass, String> annotations;


        /**
         * クラス名を指定してインスタンスを生成します。
         * @param fullyClassName フルパスのクラス名
         */
        public JavaClass(String fullyClassName) {
                if(fullyClassName.indexOf(".") == -1){
                        this.simpleName = fullyClassName;
                }else{
                        this.simpleName = this.toJavaClassName(fullyClassName.substring(fullyClassName.lastIndexOf(".") + 1));
                }
                this.fullyClassName = fullyClassName;

                this.fields = new LinkedHashSet<Field>();
                this.methods = new LinkedHashSet<Method>();
                this.interfaces = new HashSet<JavaClass>();
                this.innerClasses = new LinkedHashSet<JavaClass>();
                this.imports = new TreeSet<JavaClass>(new Comparator<JavaClass>() {
					@Override
					public int compare(JavaClass o1, JavaClass o2) {
						return o1.fullyClassName.compareTo(o2.fullyClassName);
					}
				});
                this.staticImports = new HashSet<String>();
                this.genericTypes = new JavaClass[]{};
                this.fieldMap = new HashMap<String, Integer>();
                this.annotations = new HashMap<JavaClass, String>();
        }

        /**
         * アクセス修飾子を返します。
         * @return アクセス修飾
         */
        public Qualifiers getQualifier() {
                return qualifier;
        }

        /**
         * アクセス修飾子を設定します
         * @param qualifier アクセス修飾
         */
        public void setQualifier(Qualifiers qualifier) {
                this.qualifier = qualifier;
        }

        public boolean isStatical() {
                return isStatical;
        }

        public void setStatical(boolean isStatical) {
                this.isStatical = isStatical;
        }

        /**
         * 継承可能かどうかを返します。
         * @return true可能 false不可能
         */
        public boolean isFinal() {
                return isFinal;
        }

        /**
         * 継承可能かどうかを設定します
         * @param isFinal true可能 false不可能
         */
        public void setFinal(boolean isFinal) {
                this.isFinal = isFinal;
        }

        /**
         * インターフェースかどうかを返します
         * @return trueインターフェース falseクラス
         */
        public boolean isInterface() {
                return isInterface;
        }

        /**
         * インターフェースかどうかを設定します
         * @param isInterface trueインターフェース falseクラス
         */
        public void setInterface(boolean isInterface) {
                this.isInterface = isInterface;
        }

        public boolean isInnerClass() {
                return isInnerClass;
        }

        public void setInnerClass(boolean isInnerClass) {
                this.isInnerClass = isInnerClass;
        }

        public boolean isAbstract() {
                return isAbstract;
        }

        public void setAbstract(boolean isAbstract) {
                this.isAbstract = isAbstract;
        }

        /**
         * クラス名をフルパスで返します。
         * @return フルパスのクラス名
         */
        public String getFullyClassName() {
                return fullyClassName;
        }

        /**
         * クラス名をフルパスで設定します
         * @param fullyClassName フルパスのクラス名
         */
        public void setFullyClassName(String fullyClassName) {
                this.fullyClassName = fullyClassName;
        }

        /**
         * Javadocコメントを返します
         * @return javadocコメント
         */
        public String getJavadocComment() {
                return javadocComment;
        }

        /**
         * javadocコメントを設定します
         * @param javadocComment javadocコメント
         */
        public void setJavadocComment(String javadocComment) {
                this.javadocComment = javadocComment;
        }

        /**
         * スーパークラスを返します
         * @return スーパークラス
         */
        public JavaClass getSuperClass() {
                return superClass;
        }

        /**
         * スーパークラスを設定します
         * @param superClass スーパークラス
         */
        public void setSuperClass(JavaClass superClass) {
                this.addImport(superClass);
                this.superClass = superClass;
        }

        public JavaClass getParentClass() {
                return parentClass;
        }

        public void setParentClass(JavaClass parentClass) {
                this.parentClass = parentClass;
        }

        /**
         * クラス名を返します
         * @return クラス名
         */
        public String getSimpleName(){
                return this.simpleName;
        }

        /**
         * このJavaClassのフィールドを返します
         * @return フィールド
         */
        public Set<Field> getFields() {
                return fields;
        }

        /**
         * このJavaClassにフィールドを追加します
         * @param field フィールド
         */
        public void addField(Field field) {
                this.addImport(field.getType());
                for(JavaClass an : field.getAnnotations().keySet()){
                        this.addImport(an);
                }
                Integer i = getDeplicatedFieldNameCount(field.getName());
                i++;
                if(i > 1){
                        this.fieldMap.put(field.getName(), i);
                        field.setName(field.getName() + "_" + i);
                }else{
                        this.fieldMap.put(field.getName(), 1);
                }
                this.fields.add(field);
        }

        public void addFieldWithAccesser(Field field) {
                addField(field);
                addMethod(createSetterMethod(field));
                addMethod(createGetterMethod(field));
        }

        protected Method createSetterMethod(Field field){
                Method m = new Method("set" + CodeUtils.toClassName(field.getName()));
                m.setArgmentTypes(new MethodArgment[]{new MethodArgment(field.getType(),field.getName())});
                m.addBodyLine("this." + field.getName() + " = " + field.getName());
                return m;
        }

        protected Method createGetterMethod(Field field){
                Method m = new Method("get" + CodeUtils.toClassName(field.getName()));
                m.setReturnType(field.getType());
                m.addBodyLine("return this." + field.getName());
                return m;
        }

        public int getDeplicatedFieldNameCount(String name){
                Integer i = this.fieldMap.get(name);
                if(i == null){
                        i = 0;
                }
                return i;
        }

        /**
         * このJavaClassのメソッドを返します
         * @return メソッド
         */
        public Set<Method> getMethods() {
                return methods;
        }

        public Method getConstractor(){
                Method ret = null;
                for(Method m : getMethods()){
                        if(m.isConstractor()){
                                ret = m;
                                break;
                        }
                }

                //存在しない場合デフォォルトコンストラクタを追加
                if(ret == null){
                        ret = new Method(getSimpleName());
                        ret.setConstractor(true);
                        addMethod(ret);
                }
                return ret;
        }

        /**
         * このjavaClassにメソッドを追加します
         * @param method メソッド
         */
        public void addMethod(Method method) {
                for(MethodArgment c : method.getArgmentTypes()){
                        this.addImport(c.getJavaClass());
                }

                this.addImport(method.getReturnType());

                this.methods.add(method);
        }

        /**
         * このJavaClassが実装するインターフェースを返します
         * @return インターフェース
         */
        public Set<JavaClass> getInterfaces() {
                return interfaces;
        }

        /**
         * このJavaClassが実装するインターフェースを追加します
         * @param iface インターフェース
         */
        public void addInterface(JavaClass iface) {
                this.addImport(iface);
                this.interfaces.add(iface);
        }

        public Set<JavaClass> getInnerClasses() {
                return innerClasses;
        }

        public JavaClass getInnerClass(String className){
                for(JavaClass inner : getInnerClasses()){
                        if(inner.getSimpleName().equals(className)){
                                return inner;
                        }
                }
                for(JavaClass inner : getInnerClasses()){
                        if(CodeUtils.toVariableName(inner.getSimpleName()).equals(className)){
                                return inner;
                        }
                }
                for(JavaClass inner : getInnerClasses()){
                        if(CodeUtils.toPluralForm(CodeUtils.toVariableName(inner.getSimpleName())).equals(className)){
                                return inner;
                        }
                }
                return null;
        }

        public void addInnerClass(JavaClass innerClass) {
                this.addImport(innerClass);
                this.innerClasses.add(innerClass);
        }

        /**
         * このJavaClassのインポ�?トクラスを返しま?�?
         * @return インポ�?トクラス
         */
        public Set<JavaClass> getImports() {
                Set<JavaClass> allImports = new HashSet<JavaClass>();
                allImports.addAll(imports);
                for(JavaClass jc : innerClasses){
                        allImports.addAll(jc.getImports());
                }

                return allImports;
        }

        /**
         * このJavaClassのインポートクラスを追加します。
         * @param jc インポートクラス
         */
        public void addImport(JavaClass jc) {
                if(shouldSetImport(jc)){
                        this.imports.add(jc);
                }
                for(JavaClass jt : jc.getGenericTypes()){
                        addImport(jt);
                }
        }

        /**
         * このJavaClassのstaticインポートを返します
         * @return staticインポート
         */
        public Set<String> getStaticImports() {
                return staticImports;
        }

        /**
         * このJavaClassのstaticインポートを追加します
         * @param staticImport staticインポート
         */
        public void addStaticImport(String staticImport) {
                if(isInnerClass){
                        this.parentClass.addStaticImport(staticImport);
                }else{
                        this.staticImports.add(staticImport);
                }
        }

        public JavaClass[] getGenericTypes() {
                return genericTypes;
        }

        public void setGenericTypes(JavaClass[] genericTypes) {
                this.genericTypes = genericTypes;
        }

        public String getGenericString(){
                if(genericTypes == null || genericTypes.length == 0){
                        return "";
                }

                StringBuilder sb = new StringBuilder();
                sb.append("<");
                int count = 0;
                for(JavaClass j : genericTypes){
                        if(count != 0){
                                sb.append(",");
                        }
                        sb.append(j.getSimpleName());
                }
                sb.append(">");
                return sb.toString();
        }

        public String getPackageName(){
                String s = getFullyClassName().replace(getSimpleName(), "");
                if(s.endsWith(".")){
                        s = s.substring(0,s.length()-1);
                }
                return s;
        }

        public void addAnnotaion(JavaClass jc,String argment){
                this.annotations.put(jc, argment);
                this.addImport(jc);
        }

        public Map<JavaClass, String> getAnnotations() {
                return annotations;
        }

        public String toJavaCode(){
                StringBuilder sb = new StringBuilder();
                sb.append(CodeUtils.toMultiLineJavadocComment(this.javadocComment,0));

                for(Entry<JavaClass, String> e : annotations.entrySet()){
                        sb.append("@" + e.getKey().getSimpleName());
                        if(e.getValue() != null){
                                sb.append("(");
                                sb.append(e.getValue());
                                sb.append(")");
                        }
                        sb.append("\n");
                }

                sb.append(Qualifiers.NONE != this.qualifier?this.qualifier.getName() + " ":this.qualifier.getName())
                  .append(this.isAbstract?"abstract ":"")
                  .append(this.isStatical?"static ":"")
                  .append(this.isFinal?"final ":"")
                  .append(this.isInterface?"interface ":"class ")
                  .append(this.simpleName)
                  .append(this.superClass != null?" extends " + this.superClass.getSimpleName():"");

                if(this.interfaces != null && !this.interfaces.isEmpty()){
                        sb.append(" implements ");
                        boolean isFirst = true;
                        for(JavaClass iface : this.interfaces){
                                if(!isFirst){
                                        sb.append(",");
                                }
                                sb.append(iface.getSimpleName());
                                isFirst = false;
                        }
                }

                sb.append("{\n");


                sb.append("\n");


                sb.append("}");

                return sb.toString();
        }

        /**
         * このクラスをjavaコードにして返します
         * @return javaコード
         */
        public String toJavaCodeAll(){
                StringBuilder sb = new StringBuilder();

                if(!isInnerClass){
                        this.buildPackage(sb);
                        this.buildImport(sb);
                }

                sb.append(CodeUtils.toMultiLineJavadocComment(this.javadocComment,0));

                for(Entry<JavaClass, String> e : annotations.entrySet()){
                        sb.append("@" + e.getKey().getSimpleName());
                        if(e.getValue() != null){
                                sb.append("(");
                                sb.append(e.getValue());
                                sb.append(")");
                        }
                        sb.append("\n");
                }

                sb.append(Qualifiers.NONE != this.qualifier?this.qualifier.getName() + " ":this.qualifier.getName())
                  .append(this.isAbstract?"abstract ":"")
                  .append(this.isStatical?"static ":"")
                  .append(this.isFinal?"final ":"")
                  .append(this.isInterface?"interface ":"class ")
                  .append(this.simpleName)
                  .append(this.superClass != null?" extends " + this.superClass.getSimpleName():"");

                if(this.interfaces != null && !this.interfaces.isEmpty()){
                        sb.append(" implements ");
                        boolean isFirst = true;
                        for(JavaClass iface : this.interfaces){
                                if(!isFirst){
                                        sb.append(",");
                                }
                                sb.append(iface.getSimpleName());
                                isFirst = false;
                        }
                }

                sb.append("{\n\n");

                this.buildFields(sb);

                sb.append("\n");

                this.buildMethods(sb);

                sb.append("\n");

                this.buildInnerClasses(sb);

                sb.append("}");

                return sb.toString();
        }

        public String addIndent(String src) {
                String ret = "";
                String[] srcs = src.split("\n");
                for (int i = 0; i < srcs.length; i++) {
                        ret = ret + "\t" + srcs[i] + "\n";
                }
                return ret;
        }

        private void buildPackage(StringBuilder sb){
                if(this.fullyClassName.indexOf(".") < 0){
                        return;
                }

                String pack = this.fullyClassName.substring(0,this.fullyClassName.lastIndexOf("."));
                sb.append("package ").append(pack).append(";\n\n");
        }

        private void buildImport(StringBuilder sb){
                for(String imp : this.staticImports){
                        sb.append("import static ").append(imp).append(";\n");
                }

                Set<JavaClass> allImports = new HashSet<JavaClass>();
                allImports.addAll(imports);
                for(JavaClass jc : innerClasses){
                        allImports.addAll(jc.getImports());
                }

                for(JavaClass imp : allImports){
                        sb.append("import ").append(imp.getFullyClassName()).append(";\n");
                }

                sb.append("\n");
        }

        private void buildFields(StringBuilder sb){
                for(Field field : this.fields){
                        //sb.append("\t");
                        sb.append(field.toJavaCodeAll());
                        sb.append("\n");
                }
        }

        private void buildMethods(StringBuilder sb){
                for(Method m : this.methods){
                        sb.append(m.toJavaCode());
                        sb.append("\n");
                        sb.append("\n");
                }
        }

        private void buildInnerClasses(StringBuilder sb){
                for(JavaClass jc : this.innerClasses){
                        sb.append(addIndent(jc.toJavaCodeAll()));
                        sb.append("\n");
                        sb.append("\n");
                }
        }

        private String toJavaClassName(String name){
                if("void".equals(name) || "int".equals(name) || "double".equals(name) || "long".equals(name)
                                || "float".equals(name) || "char".equals(name) || "byte".equals(name) || "short".equals(name)){
                        return name;
                }


                String first = name.substring(0, 1);
                String other = name.substring(1);

                return first.toUpperCase() + other;

        }

        private boolean shouldSetImport(JavaClass jc){
                String name = jc.getPackageName();
                if(name == null || name.equals("") ||
                                this.getPackageName().equals(name) ||
                                name.startsWith("java.lang")){
                        return false;
                }

                return true;
        }

        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result
                                + ((fullyClassName == null) ? 0 : fullyClassName.hashCode());
                return result;
        }

        @Override
        public boolean equals(Object obj) {
                if (this == obj)
                        return true;
                if (obj == null)
                        return false;
                if (getClass() != obj.getClass())
                        return false;
                JavaClass other = (JavaClass) obj;
                if (fullyClassName == null) {
                        if (other.fullyClassName != null)
                                return false;
                } else if (!fullyClassName.equals(other.fullyClassName))
                        return false;
                return true;
        }


        //リスト系のインターフェースを持つか調べま�?
        public boolean isHaveListInterFace() {
                if (interfaces == null) {
                        return false;
                }
                for (JavaClass interFaceClass : interfaces) {
                        if (interFaceClass.getFullyClassName().equals("java.util.List")
                                || interFaceClass.getFullyClassName().equals("java.util.Set")
                                || interFaceClass.getFullyClassName().equals("java.util.NavigableSet")) {
                                return true;
                        }
                }
                return false;
        }

        //マップ系のインターフェースを持つか調べます�?
        public boolean isHaveMapInterFace() {
                if (interfaces == null) {
                        return false;
                }
                for (JavaClass interFaceClass : interfaces) {
                        if (interFaceClass.getFullyClassName().equals("java.util.Map")
                                || interFaceClass.getFullyClassName().equals("java.util.NavigableMap")) {
                                return true;
                        }
                }
                return false;
        }

        public JavaClass getFiledByName(String param) {
                for (Field field : fields) {
                        if (field.getName().equals(param)) {
                                return field.getType();
                        }
                }
                return null;
        }

}