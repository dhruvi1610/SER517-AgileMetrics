package edu.asu.cassess.service.github;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weeks {

    @Column(name = "w")
    private int w;

    @Column(name = "a")
    private int a;

    @Column(name = "d")
    private int d;

    @Column(name = "c")
    private int c;

    public Weeks() {
    }

    public Weeks(int w, int a, int d, int c) {
        this.w = w;
        this.a = a;
        this.d = d;
        this.c = c;
    }

    public int getW() {
        return w;
    }

    public int getA() {
        return a;
    }

    public int getD() {
        return d;
    }

    public int getC() {
        return c;
    }

    public void setW(int w) {
        this.w = w;
    }

    public void setA(int a) {
        this.a = a;
    }

    public void setD(int d) {
        this.d = d;
    }

    public void setC(int c) {
        this.c = c;
    }

    @Override
    public String toString() {
        return "Weeks{" +
                "w=" + w +
                ", a=" + a +
                ", d=" + d +
                ", c=" + c +
                '}';
    }
}
