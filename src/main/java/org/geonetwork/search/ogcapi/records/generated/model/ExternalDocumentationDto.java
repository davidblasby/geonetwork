/*
 * (c) 2003 Open Source Geospatial Foundation - all rights reserved
 * This code is licensed under the GPL 2.0 license,
 * available at the root application directory.
 */
package org.geonetwork.search.ogcapi.records.generated.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Generated;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.Objects;

/**
 * ExternalDocumentationDto
 */

@JsonTypeName("ExternalDocumentation")
@JacksonXmlRootElement(localName = "ExternalDocumentationDto")
@XmlRootElement(name = "ExternalDocumentationDto")
@XmlAccessorType(XmlAccessType.FIELD)

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2024-07-03T16:51:11.791047085+02:00[Europe/Paris]", comments = "Generator version: 7.6.0")
public class ExternalDocumentationDto {

    private String description;

    private String url;

    public ExternalDocumentationDto() {
        super();
    }

    /**
     * Constructor with only required parameters
     */
    public ExternalDocumentationDto(String url) {
        this.url = url;
    }

    /**
     * Create a builder with no initialized field (except for the default values).
     */
    public static Builder builder() {
        return new Builder();
    }

    public ExternalDocumentationDto description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Get description
     *
     * @return description
     */

    @Schema(name = "description", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @JsonProperty("description")
    @JacksonXmlProperty(localName = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ExternalDocumentationDto url(String url) {
        this.url = url;
        return this;
    }

    /**
     * Get url
     *
     * @return url
     */
    @NotNull
    @Schema(name = "url", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("url")
    @JacksonXmlProperty(localName = "url")
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ExternalDocumentationDto externalDocumentation = (ExternalDocumentationDto) o;
        return Objects.equals(this.description, externalDocumentation.description) &&
            Objects.equals(this.url, externalDocumentation.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, url);
    }

    @Override
    public String toString() {
        String sb = "class ExternalDocumentationDto {\n" +
            "    description: " + toIndentedString(description) + "\n" +
            "    url: " + toIndentedString(url) + "\n" +
            "}";
        return sb;
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }

    /**
     * Create a builder with a shallow copy of this instance.
     */
    public Builder toBuilder() {
        Builder builder = new Builder();
        return builder.copyOf(this);
    }

    public static class Builder {

        private ExternalDocumentationDto instance;

        public Builder() {
            this(new ExternalDocumentationDto());
        }

        protected Builder(ExternalDocumentationDto instance) {
            this.instance = instance;
        }

        protected Builder copyOf(ExternalDocumentationDto value) {
            this.instance.setDescription(value.description);
            this.instance.setUrl(value.url);
            return this;
        }

        public Builder description(String description) {
            this.instance.description(description);
            return this;
        }

        public Builder url(String url) {
            this.instance.url(url);
            return this;
        }

        /**
         * returns a built ExternalDocumentationDto instance.
         * <p>
         * The builder is not reusable (NullPointerException)
         */
        public ExternalDocumentationDto build() {
            try {
                return this.instance;
            } finally {
                // ensure that this.instance is not reused
                this.instance = null;
            }
        }

        @Override
        public String toString() {
            return getClass() + "=(" + instance + ")";
        }
    }

}

