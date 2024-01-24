package com.shutterfly.services.example.model;

public class JokeUpdateRequest {
    private String setup;
    private String punchline;

    public JokeUpdateRequest(String setup,String punchline) {
        this.punchline=punchline;
        this.setup = setup;
    }

    public JokeUpdateRequest() {
        super();
    }

    /**
     * get field
     *
     * @return setup
     */
    public String getSetup() {
        return this.setup;
    }

    /**
     * set field
     *
     * @param setup
     */
    public void setSetup(String setup) {
        this.setup = setup;
    }

    /**
     * get field
     *
     * @return punchline
     */
    public String getPunchline() {
        return this.punchline;
    }

    /**
     * set field
     *
     * @param punchline
     */
    public void setPunchline(String punchline) {
        this.punchline = punchline;
    }
}
