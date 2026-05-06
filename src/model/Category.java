package model;

public enum Category {
    ENTERTAINMENT("Entertainment", "🎬"),
    EDUCATION("Education", "📚"),
    BILLS("Bills", "💡"),
    HEALTH("Health", "❤️"),
    OTHER("Other", "❓");

    private final String name;
    private final String icon;

    Category(String name, String icon)
    {
        this.name = name;
        this.icon = icon;
    }

    @Override
    public String toString()
    {
        return icon + " " + name;
    }
}
