package br.com.fecaf.model;

public class Produto {
    private String name;
    private String imagePath;
    private String description;
    private double price;

    public Produto(String name, String imagePath, String description, double price) {
        this.name = name;
        this.imagePath = imagePath;
        this.description = description;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
}

