package com.github.typesafe_query.tools.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;


/**
 * Javaクラスのメソッドを表すクラスです
 * @author MOSA Takahiko Satou
 *
 */
public final class Method implements Comparable<Method>{
        private Qualifiers qualifier = Qualifiers.PUBLIC;
        private boolean isStatical = false;
        private boolean isFinal = false;
        private boolean isConstractor = false;
        private boolean isInterfaceMethod;
        private boolean isAbstract;
        private JavaClass returnType = new JavaClass("void");
        private String name;
        private String javadocComment;
        private MethodArgment[] argmentTypes;
        private List<String> bodyLine;
        private Map<JavaClass, String> annotations;

        public Method(String name) {
                this.name = name;
                this.bodyLine = new ArrayList<String>();
                this.argmentTypes = new MethodArgment[]{};
                this.annotations = new HashMap<JavaClass, String>();
        }

        public Qualifiers getQualifier() {
                return qualifier;
        }

        public void setQualifier(Qualifiers qualifier) {
                this.qualifier = qualifier;
        }

        public boolean isStatical() {
                return isStatical;
        }

        public void setStatical(boolean isStatical) {
                this.isStatical = isStatical;
        }

        public boolean isFinal() {
                return isFinal;
        }

        public void setFinal(boolean isFinal) {
                this.isFinal = isFinal;
        }

        public boolean isConstractor() {
                return isConstractor;
        }

        public void setConstractor(boolean isConstractor) {
                this.isConstractor = isConstractor;
        }

        public boolean isInterfaceMethod() {
                return isInterfaceMethod;
        }

        public void setInterfaceMethod(boolean isInterfaceMethod) {
                this.isInterfaceMethod = isInterfaceMethod;
        }

        public JavaClass getReturnType() {
                return returnType;
        }

        public void setReturnType(JavaClass returnType) {
                this.returnType = returnType;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        /**
         * Javadocコメントを返します
         * @return javadocコメント
         */
        public String getJavadocComment() {
                return javadocComment;
        }

        /**
         * Javadocコメントを設定します
         * @param javadocComment Javadocコメント
         */
        public void setJavadocComment(String javadocComment) {
                this.javadocComment = javadocComment;
        }

        public boolean isAbstract() {
                return isAbstract;
        }

        public void setAbstract(boolean isAbstract) {
                this.isAbstract = isAbstract;
        }

        public MethodArgment[] getArgmentTypes() {
                return argmentTypes;
        }

        public void setArgmentTypes(MethodArgment... argmentTypes) {
                this.argmentTypes = argmentTypes;
        }
        
        public void addArgmentType(MethodArgment methodArgment){
                List<MethodArgment> argList = new ArrayList<MethodArgment>(Arrays.asList(this.argmentTypes));
                argList.add(methodArgment);
                this.argmentTypes = argList.toArray(new MethodArgment[]{});
        }

        public List<String> getBodyLine() {
                return bodyLine;
        }

        public void addBodyLine(String line) {
                this.bodyLine.add(line);
        }

        public void addBodyLine(String line,int indent) {
                for(int i = 0;i < indent;i++){
                        line = "\t" + line;
                }
                this.addBodyLine(line);
        }

        public void addAnnotaion(JavaClass jc,String argment){
                this.annotations.put(jc, argment);
        }

        public Map<JavaClass, String> getAnnotations() {
                return annotations;
        }

        /**
         * このメソッドをjavaコードにして返します
         * @return javaコード
         */
        public String toJavaCode(){
                StringBuilder sb = new StringBuilder();
                sb.append(CodeUtils.toMultiLineJavadocComment(this.javadocComment,1));
                for(Entry<JavaClass, String> e : annotations.entrySet()){
                        sb.append("\t");
                        sb.append("@" + e.getKey().getSimpleName());
                        if(e.getValue() != null){
                                sb.append("(");
                                sb.append(e.getValue());
                                sb.append(")");
                        }
                        sb.append("\n");
                }
                sb.append("\t");
                sb.append(Qualifiers.NONE != this.qualifier?this.qualifier.getName() + " ":this.qualifier.getName());
                if(!this.isConstractor){
                        sb.append(this.isStatical?"static ":"")
                          .append(this.isAbstract?"abstract ":"")
                          .append(this.isFinal?"final ":"")
                          .append(this.returnType.getSimpleName());
                        if(this.returnType.getGenericTypes() != null && this.returnType.getGenericTypes().length > 0){
                                sb.append("<");
                                int c = 0;
                                for(JavaClass gt : this.returnType.getGenericTypes()){
                                        if(c != 0){
                                                sb.append(",");
                                        }
                                        sb.append(gt.getSimpleName());
                                        c++;
                                }
                                sb.append(">");
                        }
                        sb.append(" ");
                }
                sb.append(this.name)
                  .append("(");
                if(this.argmentTypes != null){
                        int c = 0;
                        for(MethodArgment ar : this.argmentTypes){
                                if(c != 0){
                                        sb.append(",");
                                }
                                sb.append(ar.getJavaClass().getSimpleName());
                                if(ar.getJavaClass().getGenericTypes() != null && ar.getJavaClass().getGenericTypes().length > 0){
                                        sb.append("<");
                                        int cc = 0;
                                        for(JavaClass g : ar.getJavaClass().getGenericTypes()){
                                                if(cc != 0){
                                                        sb.append(",");
                                                }
                                                sb.append(g.getSimpleName());
                                                cc++;
                                        }
                                        sb.append(">");
                                }
                                sb.append(" " + CodeUtils.toVariableName(ar.getVariable()));
                                c++;
                        }
                }
                if(isInterfaceMethod){
                        sb.append(");");
                }else{
                        sb.append("){\n");
                        for(String line : this.bodyLine){
                                sb.append("\t");
                                sb.append("\t").append(line).append("\n");
                        }
                        sb.append("\t");
                        sb.append("}");
                }
                return sb.toString();

        }

        /**
         * このメソッドをjavaコードにして返します
         * @return javaコード
         */
        public String toJavaCodeAll(){
                StringBuilder sb = new StringBuilder();
                sb.append(CodeUtils.toMultiLineJavadocComment(this.javadocComment,1));
                for(Entry<JavaClass, String> e : annotations.entrySet()){
                        sb.append("\t");
                        sb.append("@" + e.getKey().getSimpleName());
                        if(e.getValue() != null){
                                sb.append("(");
                                sb.append(e.getValue());
                                sb.append(")");
                        }
                        sb.append("\n");
                }
                sb.append("\t");
                sb.append(Qualifiers.NONE != this.qualifier?this.qualifier.getName() + " ":this.qualifier.getName());
                if(!this.isConstractor){
                        sb.append(this.isStatical?"static ":"")
                          .append(this.isFinal?"final ":"")
                          .append(this.returnType.getSimpleName());
                        if(this.returnType.getGenericTypes() != null && this.returnType.getGenericTypes().length > 0){
                                sb.append("<");
                                int c = 0;
                                for(JavaClass gt : this.returnType.getGenericTypes()){
                                        if(c != 0){
                                                sb.append(",");
                                        }
                                        sb.append(gt.getSimpleName());
                                        c++;
                                }
                                sb.append(">");
                        }
                        sb.append(" ");
                }
                sb.append(this.name)
                  .append("(");
                if(this.argmentTypes != null){
                        int c = 0;
                        for(MethodArgment ar : this.argmentTypes){
                                if(c != 0){
                                        sb.append(",");
                                }
                                sb.append(ar.getJavaClass().getSimpleName());
                                if(ar.getJavaClass().getGenericTypes() != null && ar.getJavaClass().getGenericTypes().length > 0){
                                        sb.append("<");
                                        int cc = 0;
                                        for(JavaClass g : ar.getJavaClass().getGenericTypes()){
                                                if(cc != 0){
                                                        sb.append(",");
                                                }
                                                sb.append(g.getSimpleName());
                                                cc++;
                                        }
                                        sb.append(">");
                                }
                                sb.append(" " + CodeUtils.toVariableName(ar.getVariable()));
                                c++;
                        }
                }
                sb.append("){\n");
                for(String line : this.bodyLine){
                        sb.append("\t");
                        sb.append("\t").append(line).append("\n");
                }
                sb.append("\t");
                sb.append("}");
                return sb.toString();

        }

        /*
         * (非 Javadoc)
         * @see java.lang.Comparable#compareTo(java.lang.Object)
         */
        public int compareTo(Method m) {
                if(this.isConstractor && !m.isConstractor){
                        return -1;
                }else if(!this.isConstractor && m.isConstractor){
                        return 1;
                }
                return 0;
        }

        @Override
        public int hashCode() {
                final int prime = 31;
                int result = 1;
                result = prime * result + Arrays.hashCode(argmentTypes);
                result = prime * result
                                + ((bodyLine == null) ? 0 : bodyLine.hashCode());
                result = prime * result + (isConstractor ? 1231 : 1237);
                result = prime * result + (isFinal ? 1231 : 1237);
                result = prime * result + (isStatical ? 1231 : 1237);
                result = prime * result
                                + ((javadocComment == null) ? 0 : javadocComment.hashCode());
                result = prime * result + ((name == null) ? 0 : name.hashCode());
                result = prime * result
                                + ((qualifier == null) ? 0 : qualifier.hashCode());
                result = prime * result
                                + ((returnType == null) ? 0 : returnType.hashCode());
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
                Method other = (Method) obj;
                if (!Arrays.equals(argmentTypes, other.argmentTypes))
                        return false;
                if (bodyLine == null) {
                        if (other.bodyLine != null)
                                return false;
                } else if (!bodyLine.equals(other.bodyLine))
                        return false;
                if (isConstractor != other.isConstractor)
                        return false;
                if (isFinal != other.isFinal)
                        return false;
                if (isStatical != other.isStatical)
                        return false;
                if (javadocComment == null) {
                        if (other.javadocComment != null)
                                return false;
                } else if (!javadocComment.equals(other.javadocComment))
                        return false;
                if (name == null) {
                        if (other.name != null)
                                return false;
                } else if (!name.equals(other.name))
                        return false;
                if (qualifier != other.qualifier)
                        return false;
                if (returnType == null) {
                        if (other.returnType != null)
                                return false;
                } else if (!returnType.equals(other.returnType))
                        return false;
                return true;
        }

        public static class MethodArgment{
                private JavaClass javaClass;
                private String variable;

                public MethodArgment(JavaClass javaClass,String variable) {
                        this.javaClass = javaClass;
                        this.variable = variable;
                }

                public JavaClass getJavaClass() {
                        return javaClass;
                }
                public void setJavaClass(JavaClass javaClass) {
                        this.javaClass = javaClass;
                }
                public String getVariable() {
                        return variable;
                }
                public void setVariable(String variable) {
                        this.variable = variable;
                }
        }
}