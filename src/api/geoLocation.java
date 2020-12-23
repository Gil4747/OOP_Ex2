package api;

import java.util.Objects;

public class geoLocation implements geo_location{
    private double x,y,z;
    public geoLocation(double x0,double y0,double z0){
        this.x=x0;
        this.y=y0;
        this.z=z0;
    }
   public geoLocation(double x0,double y0){
        this.x=x0;
        this.y=y0;
    }
    /**
     * Copy constructor
     * @param g
     */
    public geoLocation(geo_location g){
        this.x=g.x();
        this.y=g.y();
        this.z=g.z();
    }
    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public double z() {
        return z;
    }
    /**
     * Calculates the distance from the position of the object to the position we got in the function.
     * @param g
     * @return the distance between the two locations.
     */
    @Override
    public double distance(geo_location g) {
        if (g==null||g==this||(g.x()==this.x&&g.y()==this.y&&g.z()==this.z))
            return 0;
        return Math.sqrt((Math.pow(this.x-g.x(),2))+(Math.pow(this.y-g.y(),2))+(Math.pow(this.z-g.z(),2)));
    }
    /**
     *Checks whether the position of the department is equal to what we got in the function.
     * @param o
     * @return true if they are equal and false if they are different.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        geoLocation that = (geoLocation) o;
        return Double.compare(that.x, x) == 0 && Double.compare(that.y, y) == 0 && Double.compare(that.z, z) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }
}
