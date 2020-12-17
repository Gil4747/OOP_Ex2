package api;

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

    @Override
    public double distance(geo_location g) {
//        double dx = this.x() - p2.x();
//        double dy = this.y() - p2.y();
//        double dz = this.z() - p2.z();
//        double t = (dx*dx+dy*dy+dz*dz);
//        return Math.sqrt(t);
//    }


        if (g==null||g==this||(g.x()==this.x&&g.y()==this.y&&g.z()==this.z))
            return 0;
        return Math.sqrt((Math.pow(this.x-g.x(),2))+(Math.pow(this.y-g.y(),2))+(Math.pow(this.z-g.z(),2)));
    }
}
