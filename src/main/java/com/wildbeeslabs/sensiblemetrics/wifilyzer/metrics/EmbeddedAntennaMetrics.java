/*
 * The MIT License
 *
 * Copyright 2018 WildBees Labs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.wildbeeslabs.sensiblemetrics.wifilyzer.metrics;

import com.wildbeeslabs.sensiblemetrics.wifilyzer.metrics.interfaces.IBaseDeviceMetrics;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Embedded antenna metrics
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 *
 */
@Data
@EqualsAndHashCode
@ToString
public class EmbeddedAntennaMetrics implements IBaseDeviceMetrics {

    public static final double DEFAULT_ANTENNA_METRICS_A = 0.42093;
    public static final double DEFAULT_ANTENNA_METRICS_B = 6.9476;
    public static final double DEFAULT_ANTENNA_METRICS_C = 0.54992;

    private double coefficientA;
    private double coefficientB;
    private double coefficientC;

    public EmbeddedAntennaMetrics() {
        this(DEFAULT_ANTENNA_METRICS_A, DEFAULT_ANTENNA_METRICS_B, DEFAULT_ANTENNA_METRICS_C);
    }

    public EmbeddedAntennaMetrics(double coefficientA, double coefficientB, double coefficientC) {
        this.coefficientA = coefficientA;
        this.coefficientB = coefficientB;
        this.coefficientC = coefficientC;
    }
}
