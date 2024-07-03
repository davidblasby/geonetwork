/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */


package org.geonetwork.index.model.record;

import lombok.Data;

import java.util.List;

@Data
public class ResourceGeometry {

    private String type;
    private List<List<Double[]>> coordinates;
}
