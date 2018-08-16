package uncoupledglovedatathings;

public class Triplet<A,B,C>{
    private A first;
    private B  second;
    private C third;

    public Triplet(A a, B b, C c){
        setFirst(a);
        setSecond(b);
        setThird(c);
    }

    public A getFirst() {
        return first;
    }

    public void setFirst(A first) {
        this.first = first;
    }

    public B getSecond() {
        return second;
    }

    public void setSecond(B second) {
        this.second = second;
    }

    public C getThird() {
        return third;
    }

    public void setThird(C third) {
        this.third = third;
    }
}
