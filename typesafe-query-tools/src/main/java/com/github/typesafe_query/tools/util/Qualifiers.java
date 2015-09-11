package com.github.typesafe_query.tools.util;

public enum Qualifiers {

        PUBLIC("public"),
        PROTECTED("protected"),
        NONE(""),
        PRIVATE("private");

        private String name;

        private Qualifiers(String name){
                this.name = name;
        }

        public String getName(){
                return this.name;
        }
}