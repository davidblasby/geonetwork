/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.links;

import static org.geonetwork.ogcapi.service.configuration.BasicLinksConfigurationTest.configureContentNegotiation;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsCatalogDto;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.NativeWebRequest;

public class BasicLinksTest {

    @Test
    public void testJsonRequest() {
        BasicLinks links = new BasicLinks();
        links.contentNegotiationManager = configureContentNegotiation();

        var page = new OgcApiRecordsCatalogDto();
        var nativewebrequest = mock(NativeWebRequest.class);
        when(nativewebrequest.getParameter("f")).thenReturn("json");

        links.addStandardLinks(
                nativewebrequest,
                "baseurl",
                "endpointPath",
                page,
                Arrays.asList("html", "json"),
                "self",
                "alternative");

        var selfLink = page.getLinks().stream()
                .filter(x -> x.getRel().equals("self"))
                .findFirst()
                .get();
        var altLink = page.getLinks().stream()
                .filter(x -> x.getRel().equals("alternative"))
                .findFirst()
                .get();

        assertEquals("text/html", altLink.getType());
        assertEquals("baseurlendpointPath?f=html", altLink.getHref().toString());

        assertEquals("application/json", selfLink.getType());
        assertEquals("baseurlendpointPath?f=json", selfLink.getHref().toString());
    }

    @Test
    public void testHtmlRequest() {
        BasicLinks links = new BasicLinks();
        links.contentNegotiationManager = configureContentNegotiation();

        var page = new OgcApiRecordsCatalogDto();
        var nativewebrequest = mock(NativeWebRequest.class);
        // defaults to html
        //    when(nativewebrequest.getParameter("f")).thenReturn("html");

        links.addStandardLinks(
                nativewebrequest,
                "baseurl",
                "endpointPath",
                page,
                Arrays.asList("html", "json"),
                "self",
                "alternative");

        var selfLink = page.getLinks().stream()
                .filter(x -> x.getRel().equals("self"))
                .findFirst()
                .get();
        var altLink = page.getLinks().stream()
                .filter(x -> x.getRel().equals("alternative"))
                .findFirst()
                .get();

        assertEquals("text/html", selfLink.getType());
        assertEquals("baseurlendpointPath?f=html", selfLink.getHref().toString());

        assertEquals("application/json", altLink.getType());
        assertEquals("baseurlendpointPath?f=json", altLink.getHref().toString());
    }
}
