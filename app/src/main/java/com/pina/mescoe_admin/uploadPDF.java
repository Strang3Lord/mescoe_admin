package com.pina.mescoe_admin;

public class uploadPDF {

    public String name;
    public String uri;

    public uploadPDF() {
    }

    public uploadPDF(String name, String uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
