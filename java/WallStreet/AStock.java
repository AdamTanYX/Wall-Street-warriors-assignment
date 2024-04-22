package WallStreet;




public class AStock implements Comparable<AStock>{


    private String symbol;
    private String name;
    private double price;

    public String getSymbol() {
        return symbol;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public AStock(String symbol,String name,double price){
        this.symbol=symbol;
        this.name=name;
        this.price=price;
    }
    public AStock(String symbol,String name){
        this.symbol=symbol;
        this.name=name;

    }
    @Override
    public int compareTo(AStock o) {
        return symbol.compareTo(o.getSymbol());
    }
}
