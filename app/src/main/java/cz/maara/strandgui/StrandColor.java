package cz.maara.strandgui;

import android.os.Parcel;
import android.os.Parcelable;

public class StrandColor implements Parcelable {

    public static final StrandColor
            BLACK = new StrandColor(0, 0, 0, "black"),
            RED = new StrandColor(63, 0, 0, "red"),
            GREEN = new StrandColor(0, 63, 0, "green"),
            BLUE = new StrandColor(0, 0, 63, "blue"),
            WHITE = new StrandColor(63, 63, 63, "white"),
            YELLOW = new StrandColor(63, 63, 0, "yellow"),
            CYAN = new StrandColor(0, 63, 63, "cyan"),
            MAGENTA = new StrandColor(63, 0, 63, "magenta")
    ;

    public final int r;
    public final int g;
    public final int b;

    public final String name;

    public StrandColor(int r, int g, int b) {
        r = r << 2;
        g = g << 2;
        b = b << 2;

        this.r = r;
        this.g = g;
        this.b = b;

        int value = (r << 16) + (g << 8) + b;

        this.name = "0x" + Integer.toHexString(value);
    }

    public StrandColor(int r, int g, int b, String name) {
        r = r << 2;
        g = g << 2;
        b = b << 2;

        this.r = r;
        this.g = g;
        this.b = b;
        this.name = name;
    }

    public StrandColor(int value) {
        this.r = (value / 0x10000) % 0x100;
        this.g = (value / 0x100) % 0x100;
        this.b = value % 0x100;

        this.name = "0x" + Integer.toHexString(value);
    }

    int getGuiValue() {
        return 0xff000000 + this.r*0x10000 + this.g*0x100 + this.b*0x1;
    }

    StrandColor cloneRed(int r) {
        return new StrandColor(r, this.g >> 2, this.b >> 2);
    }

    StrandColor cloneGreen(int g) {
        return new StrandColor(this.r >> 2, g, this.b >> 2);
    }

    StrandColor cloneBlue(int b) {
        return new StrandColor(this.r >> 2, this.g >> 2, b);
    }

    private StrandColor(Parcel in) {
        this.r = in.readInt();
        this.g = in.readInt();
        this.b = in.readInt();
        this.name = in.readString();
    }

    boolean isDark() {
        return this.r + this.g + this.b < (3*127);
    }

    StrandColor getForeground() {
        return isDark() ? WHITE : BLACK;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(r);
        out.writeInt(g);
        out.writeInt(b);
        out.writeString(name);
    }

    public static final Parcelable.Creator<StrandColor> CREATOR = new Parcelable.Creator<StrandColor>() {
        public StrandColor createFromParcel(Parcel in) {
            return new StrandColor(in);
        }

        public StrandColor[] newArray(int size) {
            return new StrandColor[size];
        }
    };

}
