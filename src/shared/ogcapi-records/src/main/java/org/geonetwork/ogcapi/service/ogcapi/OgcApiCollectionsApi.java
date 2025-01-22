/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.ogcapi.service.ogcapi;

import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsCatalogDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsGetCollections200ResponseDto;
import org.geonetwork.ogcapi.records.generated.model.OgcApiRecordsLandingPageDto;
import org.geonetwork.ogcapi.service.dataaccess.CatalogApi;
import org.geonetwork.ogcapi.service.dataaccess.simpleobjects.CatalogInfo;
import org.geonetwork.ogcapi.service.indexConvert.ElasticIndex2Catalog;
import org.geonetwork.ogcapi.service.links.CollectionPageLinks;
import org.geonetwork.ogcapi.service.links.CollectionsPageLinks;
import org.geonetwork.ogcapi.service.links.LandingPageLinks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.NativeWebRequest;

/**
 * High level implementation for the OgcApiCollectionsApi endpoints. The controller is responsible for the web-details,
 * this will handle the actual work.
 */
@Component
public class OgcApiCollectionsApi {

    @Autowired
    CatalogApi catalogApi;

    @Autowired
    LandingPageLinks landingPageLinks;

    @Autowired
    CollectionsPageLinks collectionsPageLinks;

    @Autowired
    CollectionPageLinks collectionPageLinks;

    @Autowired
    private NativeWebRequest nativeWebRequest;

    public OgcApiCollectionsApi() {}

    /**
     * gets the landing page data (DB table source for the 'portal').
     *
     * @return OgcApiRecordsLandingPageDto
     * @throws Exception bad config
     */
    public OgcApiRecordsLandingPageDto getLandingPage(NativeWebRequest nativeWebRequest) throws Exception {
        var uuid = catalogApi.getMainPortalUUID();
        if (uuid == null) {
            throw new Exception("no main portal found in DB table source");
        }
        var collectionInfo = describeCollection(uuid);
        var result = new OgcApiRecordsLandingPageDto();
        result.description(collectionInfo.getDescription()).title(collectionInfo.getTitle());

        result.setCatalogInfo(collectionInfo);

        landingPageLinks.addLinks(nativeWebRequest, uuid, result);

        return result;
    }

    /**
     * given a collectionId, get the DB/elastic catalogInfo and convert it to the final ogcapi-records output.
     *
     * @param catalogId collectionId (From user)
     * @return OgcApiRecordsCatalogDto
     * @throws Exception catalogId invalid, cannot find catalog.
     */
    public OgcApiRecordsCatalogDto describeCollection(String catalogId) throws Exception {
        var info = catalogApi.getPortalInfo(catalogId);

        var result = catalogInfoToOgcApiRecordsCatalogDto(info);

        collectionPageLinks.addLinks(nativeWebRequest, result);
        return result;
    }

    /**
     * converts the CatalogInfo (DB/elastic) into a OgcApiRecordsCatalogDto
     *
     * @param info CatalogInfo (DB `source` table + elastic json index record)
     * @return OgcApiRecordsCatalogDto
     */
    protected OgcApiRecordsCatalogDto catalogInfoToOgcApiRecordsCatalogDto(CatalogInfo info) {
        OgcApiRecordsCatalogDto result = new OgcApiRecordsCatalogDto();
        result.setId(info.getSource().getUuid());
        result.setTitle(info.getSource().getName());

        if (info.getLinkedIndexRecord() != null) {
            ElasticIndex2Catalog.injectLinkedServiceRecordInfo(result, info.getLinkedIndexRecord(), "eng");
            result.setGeoNetworkElasticIndexRecord(info.getLinkedIndexRecord());
        }

        return result;
    }

    /**
     * get info about ALL collections.
     *
     * @return OgcApiRecordsGetCollections200ResponseDto
     */
    public OgcApiRecordsGetCollections200ResponseDto getCollections() {
        var collectionInfos = catalogApi.getAllPortalInfos();
        var collections = collectionInfos.stream()
                .map(x -> catalogInfoToOgcApiRecordsCatalogDto(x))
                .toList();

        var result = new OgcApiRecordsGetCollections200ResponseDto();
        result.setCollections(collections);

        collectionsPageLinks.addLinks(nativeWebRequest, result);
        return result;
    }
}
