package pharmacy.reference.spring_server.entitis.reports;

public class Overall {

    public final int callCount;

    public final float sumPrice;


    public Overall(int callCount, float sumPrice) {
        this.callCount = callCount;
        this.sumPrice = sumPrice;
    }
}
