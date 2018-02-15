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
package com.wildbeeslabs.sensiblemetrics.wifilyzer.analyzer;

import com.wildbeeslabs.sensiblemetrics.wifilyzer.CmdLineProcessor;
import com.wildbeeslabs.sensiblemetrics.wifilyzer.analyzer.interfaces.IBaseNetworkAnalyzer;
import com.wildbeeslabs.sensiblemetrics.wifilyzer.entities.BeaconEntity;
import com.wildbeeslabs.sensiblemetrics.wifilyzer.filter.KalmanFilter;
import com.wildbeeslabs.sensiblemetrics.wifilyzer.metrics.EmbeddedAntennaMetrics;
import com.wildbeeslabs.sensiblemetrics.wifilyzer.utils.FileUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Base network entity class
 *
 * @author alexander.rogalskiy
 * @version 1.0
 * @since 2017-12-12
 *
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class BaseNetworkAnalyzer implements IBaseNetworkAnalyzer {

    /**
     * Default logger instance
     */
    protected final Logger LOGGER = LogManager.getLogger(getClass());
    /**
     * Default data delimiter
     */
    public static final String DEFAULT_TOKEN_DELIMITER = "[,./?;:!-\"\\s]+?";

    final CmdLineProcessor processor;

    public BaseNetworkAnalyzer(final CmdLineProcessor processor) {
        getLogger().debug("Initializing base network analyzer...");
        this.processor = processor;
    }

    protected <E> Stream<E> getFilteredStream(final Stream<E> stream, final Function<CharSequence, CharSequence> tokenFilter, final String tokenDelim) {
        return stream.flatMap(line -> Arrays.stream(String.valueOf(line).trim().split(tokenDelim)))
                .map(item -> tokenFilter.apply(item))
                .filter(StringUtils::isNotBlank)
                .map(item -> (E) item);
    }

    @Override
    public void process(int i) {
        final KalmanFilter kalmanFilter = new KalmanFilter();
        final List<String> input = FileUtils.readAllLines(this.processor.getInputSource());
        final List<Integer> list = getFilteredStream(input.stream(), this.getDefaultFilter(), BaseNetworkAnalyzer.DEFAULT_TOKEN_DELIMITER).mapToInt(Integer::parseInt).boxed().collect(Collectors.toList());
        //list.forEach(d -> System.out.println(d * -1 + "," + (int) kalmanFilter.applyFilter(d) * -1));

        final BeaconEntity beaconEntity = new BeaconEntity(this.processor.getBssid(), -59);
        beaconEntity.setDeviceMetrics(new EmbeddedAntennaMetrics());
        beaconEntity.setRssiFilter(new KalmanFilter());
        final List<String> result = new ArrayList<>();
        list.forEach(d -> {
            beaconEntity.setRssi(d);
            result.add(d * -1 + "," + (int) kalmanFilter.applyFilter(d) * -1 + "," + (int) beaconEntity.getDistance());
        });
        if (Objects.nonNull(this.processor.getOutputSource())) {
            FileUtils.writeFile(this.processor.getOutputSource(), result);
        }
    }

    protected Logger getLogger() {
        return this.LOGGER;
    }

    protected Function<CharSequence, CharSequence> getDefaultFilter() {
        return ((word) -> String.valueOf(word).toLowerCase().trim());
    }
}
