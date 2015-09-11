package com.github.typesafe_query.tools.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * Javaクラスのフィールドを表すクラスです。
 * @author MOSA Takahiko Satou
 *
 */
public class Field implements Comparable<Field>{
        private Qualifiers qualifier = Qualifiers.PRIVATE;
        private boolean isStatical;
        private boolean isFinal;
        private JavaClass type;
        private String name;
        private String initializeString;
        private String javadocComment;
        private Map<JavaClass, String> annotations;

        /**
         * 変数名を設定してインスタンスを生成します
         * @param name 変数名
         */
        public Field(String name) {
                this.name = name;
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

        /**
         * クラス変数かどうかを返します
         * @return trueクラス変数 falseインスタンス変数
         */
        public boolean isStatical() {
                return isStatical;
        }

        /**
         * クラス変数かどうかを設定します
         * @param isStatical trueクラス変数 falseインスタンス変数
         */
        public void setStatical(boolean isStatical) {
                this.isStatical = isStatical;
        }

        /**
         * 変更可能かどうかを返します
         * @return true可能 false不可能
         */
        public boolean isFinal() {
                return isFinal;
        }

        /**
         * 変更可能かどうかを設定します。
         * @param isFinal true可能 false不可能
         */
        public void setFinal(boolean isFinal) {
                this.isFinal = isFinal;
        }

        /**
         * 型を返します。
         * @return Fieldのタイプ
         */
        public JavaClass getType() {
                return type;
        }

        /**
         * 型を設定します。
         * @param type このFieldの型
         */
        public void setType(JavaClass type) {
                this.type = type;
        }

        /**
         * 変数名を返します。
         * @return このFieldの変数名
         */
        public String getName() {
                return name;
        }

        /**
         * 変数名を設定します
         * @param name このFieldの変数
         */
        public void setName(String name) {
                this.name = name;
        }

        /**
         * 初期化部�?を返しま?�?
         * @return 初期化
         */
        public String getInitializeString() {
                return initializeString;
        }

        /**
         * 初期化部設定します�?
         * @param initializeString 初期化部
         */
        public void setInitializeString(String initializeString) {
                this.initializeString = initializeString;
        }

        /**
         * Javadocコメントを返します
         * @return javadocコメント
         */
        public String getJavadocComment() {
                return javadocComment;
        }

        /**
         * Javadocコメントを設定します。
         * @param javadocComment Javadocコメント
         */
        public void setJavadocComment(String javadocComment) {
                this.javadocComment = javadocComment;
        }

        public void addAnnotaion(JavaClass jc,String argment){
                this.annotations.put(jc, argment);
        }

        public Map<JavaClass, String> getAnnotations() {
                return annotations;
        }

        public String toJavaCode(){
                StringBuilder sb = new StringBuilder();
                sb.append(CodeUtils.toJavadocComment(this.javadocComment));

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
                  .append(this.isStatical?"static ":"")
                  .append(this.isFinal?"final ":"")
                  .append(this.type.getSimpleName());
                if(this.type.getGenericTypes() != null && this.type.getGenericTypes().length > 0){
                        sb.append("<");
                        int c = 0;
                        for(JavaClass gt : this.type.getGenericTypes()){
                                if(c != 0){
                                        sb.append(",");
                                }
                                sb.append(gt.getSimpleName());
                                c++;
                        }
                        sb.append(">");
                }

                sb.append(" ")
                  .append(this.name)
                  .append(this.initializeString != null?" = " + this.initializeString:"")
                  .append(";");
                return sb.toString();
        }

        /**
         * このFieldをjavaコードにして返します。
         * @return javaコード
         */
        public String toJavaCodeAll(){
                StringBuilder sb = new StringBuilder();
                sb.append(CodeUtils.toJavadocComment(this.javadocComment));

                for(Entry<JavaClass, String> e : annotations.entrySet()){
                        sb.append("\t" + "@" + e.getKey().getSimpleName());
                        if(e.getValue() != null){
                                sb.append("(");
                                sb.append(e.getValue());
                                sb.append(")");
                        }
                        sb.append("\n");
                }

                sb.append("\t")
                  .append(Qualifiers.NONE != this.qualifier?this.qualifier.getName() + " ":this.qualifier.getName())
                  .append(this.isStatical?"static ":"")
                  .append(this.isFinal?"final ":"")
                  .append(this.type.getSimpleName());
                if(this.type.getGenericTypes() != null && this.type.getGenericTypes().length > 0){
                        sb.append("<");
                        int c = 0;
                        for(JavaClass gt : this.type.getGenericTypes()){
                                if(c != 0){
                                        sb.append(",");
                                }
                                sb.append(gt.getSimpleName());
                                c++;
                        }
                        sb.append(">");
                }

                sb.append(" ")
                  .append(this.name)
                  .append(this.initializeString != null?" = " + this.initializeString:"")
                  .append(";");
                return sb.toString();
        }

        public int compareTo(Field f) {
                if(this.qualifier == Qualifiers.PUBLIC
                                && this.isStatical
                                && this.isFinal
                                && (f.qualifier != Qualifiers.PUBLIC
                                                || !f.isStatical
                                                || !f.isFinal)){
                        return -1;
                }
                return 0;
        }


        /**
         * マップ系のインターフェースを持つか調べます。
         * @return 持っていたらtrue
         */
        public boolean isHaveMapInterFace() {
                if (type == null) {
                        return false;
                }
                Set<JavaClass> interFaces = type.getInterfaces();
                for (JavaClass interFaceClass : interFaces) {
                        if (interFaceClass.getFullyClassName().equals("java.util.Map")
                                || interFaceClass.getFullyClassName().equals("java.util.NavigableMap")) {
                                return true;
                        }
                }
                return false;
        }

        /**
         * リスト系のインターフェースを持つか調べます。
         * @return
         */
        public boolean isHaveListInterFace() {
                if (type == null) {
                        return false;
                }
                Set<JavaClass> interFaces = type.getInterfaces();
                for (JavaClass interFaceClass : interFaces) {
                        if (interFaceClass.getFullyClassName().equals("java.util.List")
                                || interFaceClass.getFullyClassName().equals("java.util.Set")
                                || interFaceClass.getFullyClassName().equals("java.util.NavigableSet")) {
                                return true;
                        }
                }
                return false;
        }

}