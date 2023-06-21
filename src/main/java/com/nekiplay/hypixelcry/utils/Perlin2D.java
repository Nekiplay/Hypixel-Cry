package com.nekiplay.hypixelcry.utils;

import java.util.Random;

public class Perlin2D {
    public static double PerlinNoice(double multiply) {
        Perlin2D perlin = new Perlin2D(new Random().nextInt());
        float Phi = 0.70710678118f;
        float noice = perlin.Noise(5, 5) + perlin.Noise((25 - 25) * Phi, (25 + 25) * Phi) * -1;
        return noice * multiply;
    }


    byte[] permutationTable;

    public Perlin2D(int seed)
    {
        Random rand = new Random(seed);
        permutationTable = new byte[1024];
        rand.nextBytes(permutationTable);
    }

    public Perlin2D()
    {
        Random rand = new Random(0);
        permutationTable = new byte[1024];
        rand.nextBytes(permutationTable);
    }

    private float[] GetPseudoRandomGradientVector(int x, int y)
    {
        int v = (int)(((x * 1836311903L) ^ (y * 2971215073L) + 4807526976L) & 1023);
        v = permutationTable[v] & 3;

        if (v == 0) {
            return new float[]{1, 0};
        }
        else if (v == 1) {
            return new float[]{-1, 0};
        }
        else if (v == 2) {
            return new float[]{0, 1};
        }
        else {
            return new float[]{0, -1};
        }
    }

    static float QunticCurve(float t)
    {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    static float Lerp(float a, float b, float t)
    {
        return a + (b - a) * t;
    }

    static float Dot(float[] a, float[] b)
    {
        return a[0] * b[0] + a[1] * b[1];
    }

    public float Noise(float fx, float fy)
    {
        int left = (int)Math.floor(fx);
        int top = (int)Math.floor(fy);
        float pointInQuadX = fx - left;
        float pointInQuadY = fy - top;

        float[] topLeftGradient = GetPseudoRandomGradientVector(left, top);
        float[] topRightGradient = GetPseudoRandomGradientVector(left + 1, top);
        float[] bottomLeftGradient = GetPseudoRandomGradientVector(left, top + 1);
        float[] bottomRightGradient = GetPseudoRandomGradientVector(left + 1, top + 1);

        float[] distanceToTopLeft = new float[] { pointInQuadX, pointInQuadY };
        float[] distanceToTopRight = new float[] { pointInQuadX - 1, pointInQuadY };
        float[] distanceToBottomLeft = new float[] { pointInQuadX, pointInQuadY - 1 };
        float[] distanceToBottomRight = new float[] { pointInQuadX - 1, pointInQuadY - 1 };

        float tx1 = Dot(distanceToTopLeft, topLeftGradient);
        float tx2 = Dot(distanceToTopRight, topRightGradient);
        float bx1 = Dot(distanceToBottomLeft, bottomLeftGradient);
        float bx2 = Dot(distanceToBottomRight, bottomRightGradient);

        pointInQuadX = QunticCurve(pointInQuadX);
        pointInQuadY = QunticCurve(pointInQuadY);

        float tx = Lerp(tx1, tx2, pointInQuadX);
        float bx = Lerp(bx1, bx2, pointInQuadX);
        float tb = Lerp(tx, bx, pointInQuadY);

        return tb;
    }

    public float Noise(float fx, float fy, int octaves, float persistence)
    {
        float amplitude = 1;
        float max = 0;
        float result = 0;

        while (octaves-- > 0)
        {
            max += amplitude;
            result += Noise(fx, fy) * amplitude;
            amplitude *= persistence;
            fx *= 2;
            fy *= 2;
        }

        return result / max;
    }
}
