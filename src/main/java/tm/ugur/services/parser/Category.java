package tm.ugur.services.parser;

import org.springframework.stereotype.Component;

public class Category {

    private String name_tm;
    private String name_ru;

    public Category(String name_tm, String name_ru) {
        this.name_tm = name_tm;
        this.name_ru = name_ru;
    }

    public String getName_tm() {
        return name_tm;
    }

    public void setName_tm(String name_tm) {
        this.name_tm = name_tm;
    }

    public String getName_ru() {
        return name_ru;
    }

    public void setName_ru(String name_ru) {
        this.name_ru = name_ru;
    }


    @Override
    public String toString() {
        return "Category{" +
                ", name_tm='" + name_tm + '\'' +
                ", name_ru='" + name_ru + '\'' +
                '}';
    }
}
