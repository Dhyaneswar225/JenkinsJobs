package com.shutterfly.services.example.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Getter
@Setter
@Document(collection = "jokes")
public class Joke {
    @Id
    private String id;
    private String type;
    private String setup;
    private String punchline;
    public Joke() {
        super();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPunchline() {
        return punchline;
    }

    public void setPunchline(String punchline) {
        this.punchline = punchline;
    }

    public String getType() {
        return type;
    }

    public String getSetup() {
        return setup;
    }

    public void setSetup(String setup) {
        this.setup = setup;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Joke(String type, String setup, String punchline){
        this.type=type;
        this.setup=setup;
        this.punchline=punchline;
    }


}
