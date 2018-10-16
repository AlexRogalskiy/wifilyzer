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
package com.wildbeeslabs.sensiblemetrics.wifilyzer.filter;

import com.wildbeeslabs.sensiblemetrics.wifilyzer.filter.interfaces.IBaseFilter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Kalman filter class
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 *
 */
@Data
@EqualsAndHashCode
@ToString
public class KalmanFilter implements IBaseFilter<Double, Double> {

    /**
     * Process noise
     */
    private double processNoise;
    /**
     * Measurement noise
     */
    private double measurementNoise;
    /**
     * Calculated RSSI
     */
    private double estimatedRSSI;
    /**
     * Calculated covariance
     */
    private double errorCovarianceRSSI;
    /**
     * Initialization flag
     */
    private boolean isInitialized = false;

    public KalmanFilter() {
        this(0.125, 0.8);
    }

    public KalmanFilter(double processNoise, double measurementNoise) {
        this.processNoise = processNoise;
        this.measurementNoise = measurementNoise;
    }

    @Override
    public Double applyFilter(final Double rssi) {
        double priorRSSI;
        double priorErrorCovarianceRSSI;
        if (!this.isInitialized) {
            priorRSSI = rssi;
            priorErrorCovarianceRSSI = 1;
            this.isInitialized = true;
        } else {
            priorRSSI = this.estimatedRSSI;
            priorErrorCovarianceRSSI = this.errorCovarianceRSSI + this.processNoise;
        }
        double kalmanGain = priorErrorCovarianceRSSI / (priorErrorCovarianceRSSI + this.measurementNoise);
        this.estimatedRSSI = priorRSSI + (kalmanGain * (rssi - priorRSSI));
        this.errorCovarianceRSSI = (1 - kalmanGain) * priorErrorCovarianceRSSI;
        return this.estimatedRSSI;
    }
}
