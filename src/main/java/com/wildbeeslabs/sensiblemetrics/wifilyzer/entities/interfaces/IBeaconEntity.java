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
package com.wildbeeslabs.sensiblemetrics.wifilyzer.entities.interfaces;

import com.wildbeeslabs.sensiblemetrics.wifilyzer.filter.interfaces.IBaseFilter;
import com.wildbeeslabs.sensiblemetrics.wifilyzer.metrics.interfaces.IBaseDeviceMetrics;

/**
 * Beacon interface declaration
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 *
 */
public interface IBeaconEntity {

    double getDistance();

    String getMacAddress();

    void setMacAddress(final String macAddress);

    double getRssi();

    void setRssi(double rssi);

    int getTxPower();

    void setTxPower(int txPower);

    IBaseFilter getRssiFilter();

    void setRssiFilter(final IBaseFilter filter);

    IBaseDeviceMetrics getDeviceMetrics();

    void setDeviceMetrics(final IBaseDeviceMetrics deviceMetrics);

    void calculateDistanceFromRssi();
}
