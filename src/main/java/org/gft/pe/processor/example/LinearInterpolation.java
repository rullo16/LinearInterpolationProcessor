/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.gft.pe.processor.example;

import org.apache.streampipes.model.runtime.Event;
import org.apache.streampipes.model.schema.EventSchema;
import org.apache.streampipes.wrapper.context.EventProcessorRuntimeContext;
import org.apache.streampipes.wrapper.routing.SpOutputCollector;
import org.apache.streampipes.wrapper.runtime.EventProcessor;

import java.util.List;

public class LinearInterpolation implements EventProcessor<LinearInterpolationParameters> {

    private EventSchema outputSchema;
    private List<String> outputKeySelectors;

    private String timeStampField;

    private static double[] interpolate(double start, double end, int count) {
        double[] array = new double[count + 1];
        for (int i = 0; i < count; i++) {
            array[i] = start + i * (end - start) / count;
        }
        return array;
    }

    @Override
    public void onInvocation(LinearInterpolationParameters parameters,
                             SpOutputCollector out, EventProcessorRuntimeContext ctx) {

        this.outputSchema = parameters.getGraph().getOutputStream().getEventSchema();
        this.outputKeySelectors = parameters.getOutputKeySelector();
        this.timeStampField = parameters.getTimeStamp();

    }

    @Override
    public void onEvent(Event event, SpOutputCollector out) {


        double[] linearInterpolation = interpolate();


        event.addField("InterPolation", linearInterpolation);
        out.collect(event);
    }

    @Override
    public void onDetach() {

    }
}
