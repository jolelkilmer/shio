/*
 * Copyright (C) 2016-2020 the original author or authors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.viglet.shio.provider.exchange.otmm.bean.object;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.viglet.shio.provider.exchange.otmm.bean.assets.ShOTMMAssetContentInfoBean;
import com.viglet.shio.provider.exchange.otmm.bean.permission.ShOTMMAccessControlDescriptorBean;
import java.util.Date;

/**
 * @author Alexandre Oliveira
 * @since 0.3.7
 */
public abstract class ShOTMMObjectBean {

  @JsonProperty("access_control_descriptor")
  private ShOTMMAccessControlDescriptorBean accessControlDescriptor;

  @JsonProperty("asset_content_info")
  private ShOTMMAssetContentInfoBean assetContentInfo;

  @JsonProperty("asset_id")
  private String assetId;

  @JsonProperty("asset_state")
  private String assetState;

  @JsonProperty("asset_state_last_update_date")
  private Date assetStateLastUpdateDate;

  @JsonProperty("asset_state_user_id")
  private String assetStateUserId;

  @JsonProperty("checked_out")
  private boolean checkedOut;

  @JsonProperty("content_editable")
  private boolean contentEditable;

  @JsonProperty("content_state")
  private String contentState;

  @JsonProperty("content_type")
  private String contentType;

  @JsonProperty("creator_id")
  private String creatorId;

  @JsonProperty("date_imported")
  private Date dateImported;

  @JsonProperty("date_last_updated")
  private Date dateLastUpdated;

  private boolean deleted;

  private boolean expired;

  @JsonProperty("import_job_id")
  private int importJobId;

  @JsonProperty("import_user_name")
  private String importUserName;

  @JsonProperty("latest_version")
  private boolean latestVersion;

  @JsonProperty("legacy_model_id")
  private int legacyModelId;

  private boolean locked;

  @JsonProperty("metadata_model_id")
  private String metadataModelId;

  @JsonProperty("metadata_state_user_name")
  private String metadataStateUserName;

  private String name;

  @JsonProperty("original_asset_id")
  private String originalAssetId;

  @JsonProperty("subscribed_to")
  private boolean subscribedTo;

  private int version;

  public ShOTMMAccessControlDescriptorBean getAccessControlDescriptor() {
    return accessControlDescriptor;
  }

  public void setAccessControlDescriptor(
      ShOTMMAccessControlDescriptorBean accessControlDescriptor) {
    this.accessControlDescriptor = accessControlDescriptor;
  }

  public ShOTMMAssetContentInfoBean getAssetContentInfo() {
    return assetContentInfo;
  }

  public void setAssetContentInfo(ShOTMMAssetContentInfoBean assetContentInfo) {
    this.assetContentInfo = assetContentInfo;
  }

  public String getAssetId() {
    return assetId;
  }

  public void setAssetId(String assetId) {
    this.assetId = assetId;
  }

  public String getAssetState() {
    return assetState;
  }

  public void setAssetState(String assetState) {
    this.assetState = assetState;
  }

  public Date getAssetStateLastUpdateDate() {
    return assetStateLastUpdateDate;
  }

  public void setAssetStateLastUpdateDate(Date assetStateLastUpdateDate) {
    this.assetStateLastUpdateDate = assetStateLastUpdateDate;
  }

  public String getAssetStateUserId() {
    return assetStateUserId;
  }

  public void setAssetStateUserId(String assetStateUserId) {
    this.assetStateUserId = assetStateUserId;
  }

  public boolean isCheckedOut() {
    return checkedOut;
  }

  public void setCheckedOut(boolean checkedOut) {
    this.checkedOut = checkedOut;
  }

  public boolean isContentEditable() {
    return contentEditable;
  }

  public void setContentEditable(boolean contentEditable) {
    this.contentEditable = contentEditable;
  }

  public String getContentState() {
    return contentState;
  }

  public void setContentState(String contentState) {
    this.contentState = contentState;
  }

  public String getContentType() {
    return contentType;
  }

  public void setContentType(String contentType) {
    this.contentType = contentType;
  }

  public String getCreatorId() {
    return creatorId;
  }

  public void setCreatorId(String creatorId) {
    this.creatorId = creatorId;
  }

  public Date getDateImported() {
    return dateImported;
  }

  public void setDateImported(Date dateImported) {
    this.dateImported = dateImported;
  }

  public Date getDateLastUpdated() {
    return dateLastUpdated;
  }

  public void setDateLastUpdated(Date dateLastUpdated) {
    this.dateLastUpdated = dateLastUpdated;
  }

  public boolean isDeleted() {
    return deleted;
  }

  public void setDeleted(boolean deleted) {
    this.deleted = deleted;
  }

  public boolean isExpired() {
    return expired;
  }

  public void setExpired(boolean expired) {
    this.expired = expired;
  }

  public int getImportJobId() {
    return importJobId;
  }

  public void setImportJobId(int importJobId) {
    this.importJobId = importJobId;
  }

  public String getImportUserName() {
    return importUserName;
  }

  public void setImportUserName(String importUserName) {
    this.importUserName = importUserName;
  }

  public boolean isLatestVersion() {
    return latestVersion;
  }

  public void setLatestVersion(boolean latestVersion) {
    this.latestVersion = latestVersion;
  }

  public int getLegacyModelId() {
    return legacyModelId;
  }

  public void setLegacyModelId(int legacyModelId) {
    this.legacyModelId = legacyModelId;
  }

  public boolean isLocked() {
    return locked;
  }

  public void setLocked(boolean locked) {
    this.locked = locked;
  }

  public String getMetadataModelId() {
    return metadataModelId;
  }

  public void setMetadataModelId(String metadataModelId) {
    this.metadataModelId = metadataModelId;
  }

  public String getMetadataStateUserName() {
    return metadataStateUserName;
  }

  public void setMetadataStateUserName(String metadataStateUserName) {
    this.metadataStateUserName = metadataStateUserName;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getOriginalAssetId() {
    return originalAssetId;
  }

  public void setOriginalAssetId(String originalAssetId) {
    this.originalAssetId = originalAssetId;
  }

  public boolean isSubscribedTo() {
    return subscribedTo;
  }

  public void setSubscribedTo(boolean subscribedTo) {
    this.subscribedTo = subscribedTo;
  }

  public int getVersion() {
    return version;
  }

  public void setVersion(int version) {
    this.version = version;
  }
}
