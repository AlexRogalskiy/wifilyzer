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
package com.wildbeeslabs.sensiblemetrics.wifilyzer.entities;

import com.wildbeeslabs.sensiblemetrics.wifilyzer.entities.interfaces.INetworkEntity;
import com.wildbeeslabs.sensiblemetrics.wifilyzer.entities.interfaces.INetworkConfiguration;
import com.wildbeeslabs.sensiblemetrics.wifilyzer.entities.interfaces.INetworkLocation;
import com.wildbeeslabs.sensiblemetrics.wifilyzer.filter.interfaces.IBaseFilter;
import com.wildbeeslabs.sensiblemetrics.wifilyzer.metrics.interfaces.IBaseDeviceMetrics;

import java.time.LocalDateTime;

import java.util.Objects;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * Network model
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 *
 */
@Data
@EqualsAndHashCode
@ToString
public class NetworkEntity implements INetworkEntity {

    private double distance;
    private String bssid;
    private String ssid;
    private double rssi;
    private int channel;
    private double frequency;
    private int txPower;
    private LocalDateTime timestamp;

    private boolean isFilterApplied = false;
    private boolean isDistanceCalculated = false;

    private INetworkLocation location;
    private INetworkConfiguration configuration;

    private IBaseFilter<Double, Double> rssiFilter;
    private IBaseDeviceMetrics deviceMetrics;

    public NetworkEntity(final String bssid, int txPower) {
        this.bssid = bssid;
        this.txPower = txPower;
    }

    public NetworkEntity(final String bssid, double rssi, int txPower) {
        this.bssid = bssid;
        this.rssi = rssi;
        this.txPower = txPower;
    }

    public NetworkEntity(final String macAddress, double rssi, int txPower, final IBaseFilter<Double, Double> rssiFilter) {
        this(macAddress, rssi, txPower);
        this.rssiFilter = rssiFilter;
    }

    public NetworkEntity(final String macAddress, double rssi, int txPower, final IBaseDeviceMetrics deviceMetrics) {
        this(macAddress, rssi, txPower);
        this.deviceMetrics = deviceMetrics;
    }

    public NetworkEntity(final String macAddress, double rssi, int txPower, final IBaseFilter<Double, Double> rssiFilter, final IBaseDeviceMetrics deviceMetrics) {
        this(macAddress, rssi, txPower);
        this.rssiFilter = rssiFilter;
        this.deviceMetrics = deviceMetrics;
    }

    @Override
    public double getDistance() {
        if (!this.isFilterApplied) {
            this.applyFilter();
        }
        if (!this.isDistanceCalculated) {
            this.calculateDistanceFromRssi();
        }
        return this.distance;
    }

    @Override
    public void setRssi(double rssi) {
        this.isFilterApplied = false;
        this.isDistanceCalculated = false;
        this.rssi = rssi;
        this.applyFilter();
    }

    @Override
    public void calculateDistanceFromRssi() {
        if (Objects.isNull(this.deviceMetrics)) {
            throw new IllegalStateException("ERROR: measurement device constants must be set before calculateDistanceFromRssi operation call");
        }
        if (this.rssi == 0) {
            this.distance = -1.0;
        }

        double ratio = this.rssi * 1.0 / this.txPower;
        if (ratio < 1.0) {
            this.distance = Math.pow(ratio, 10);
        } else {
            this.distance = (deviceMetrics.getCoefficientA()) * Math.pow(ratio, this.deviceMetrics.getCoefficientB()) + this.deviceMetrics.getCoefficientC();
        }
        this.isDistanceCalculated = true;
    }

    public void applyFilter() {
        if (Objects.isNull(this.rssiFilter)) {
            throw new IllegalStateException("ERROR: rssi filter must be set before applyFilter operation call");
        }
        this.rssi = this.rssiFilter.applyFilter(this.rssi);
        this.isFilterApplied = true;
    }
}
