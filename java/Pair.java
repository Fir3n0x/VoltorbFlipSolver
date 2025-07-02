public class Pair<K,V> {
    private K point;
    private V volt;

    public Pair(K point, V volt){
        this.point = point;
        this.volt = volt;
    }

    public K getPoint(){
        return point;
    }

    public V getVolt(){
        return volt;
    }

    public Pair<K,V> getBoth(){
        return this;
    }

    public void setPoint(K p){
        point = p;
    }

    public void setVolt(V v){
        volt = v;
    }

    public void setBoth(V v, K p){
        point = p; volt = v;
    }

    public String toString(){
        return "(" + point + ", " + volt + ")";
    }
}
