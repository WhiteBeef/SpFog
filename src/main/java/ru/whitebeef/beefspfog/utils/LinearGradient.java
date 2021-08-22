package ru.whitebeef.beefspfog.utils;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;

public class LinearGradient {

    private final ChatColor color1;
    private final ChatColor color2;
    private final ChatColor color3;
    private final int maxDy;

    public LinearGradient(ChatColor color1, ChatColor color2, ChatColor color3, int maxDy) {
        this.color1 = color1;
        this.color2 = color2;
        this.color3 = color3;
        this.maxDy = maxDy;
    }

    public ChatColor getColor(double dY) {
        if (dY <= 0)
            return this.color1;
        else if (dY >= this.maxDy)
            return this.color3;
        else {
            double start;
            double end;
            double r1;
            double g1;
            double b1;
            double r2;
            double g2;
            double b2;
            if (dY <= this.maxDy / 2d) {
                start = 0;
                end = this.maxDy / 2d;
                r1 = this.color1.getColor().getRed();
                g1 = this.color1.getColor().getGreen();
                b1 = this.color1.getColor().getBlue();
                r2 = this.color2.getColor().getRed();
                g2 = this.color2.getColor().getGreen();
                b2 = this.color2.getColor().getBlue();
            } else {
                start = this.maxDy / 2d;
                end = this.maxDy;
                r1 = this.color2.getColor().getRed();
                g1 = this.color2.getColor().getGreen();
                b1 = this.color2.getColor().getBlue();
                r2 = this.color3.getColor().getRed();
                g2 = this.color3.getColor().getGreen();
                b2 = this.color3.getColor().getBlue();
            }
            double r = (dY - start) * (r2 - r1) / (end - start) + r1;
            double g = (dY - start) * (g2 - g1) / (end - start) + g1;
            double b = (dY - start) * (b2 - b1) / (end - start) + b1;
            return ChatColor.of(new Color((int) r, (int) g, (int) b));
        }
    }

}
