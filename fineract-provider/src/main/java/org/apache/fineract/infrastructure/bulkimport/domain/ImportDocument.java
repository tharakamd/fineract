/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.fineract.infrastructure.bulkimport.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import org.apache.fineract.infrastructure.core.domain.AbstractPersistableCustom;
import org.apache.fineract.infrastructure.documentmanagement.domain.Document;
import org.apache.fineract.useradministration.domain.AppUser;

@Entity
@Table(name = "m_import_document")
public class ImportDocument extends AbstractPersistableCustom {

    @OneToOne
    @JoinColumn(name = "document_id")
    private Document document;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "import_time")
    private Date importTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "completed", nullable = false)
    private Boolean completed;

    @Column(name = "entity_type")
    private Integer entityType;

    @ManyToOne
    @JoinColumn(name = "createdby_id")
    private AppUser createdBy;

    @Column(name = "total_records", nullable = true)
    private Integer totalRecords;

    @Column(name = "success_count", nullable = true)
    private Integer successCount;

    @Column(name = "failure_count", nullable = true)
    private Integer failureCount;

    protected ImportDocument() {

    }

    public static ImportDocument instance(final Document document, final LocalDateTime importTime, final Integer entityType,
            final AppUser createdBy, final Integer totalRecords) {

        final Boolean completed = Boolean.FALSE;
        final Integer successCount = 0;
        final Integer failureCount = 0;
        final LocalDateTime endTime = LocalDateTime.now(ZoneId.systemDefault());

        return new ImportDocument(document, importTime, endTime, completed, entityType, createdBy, totalRecords, successCount,
                failureCount);
    }

    private ImportDocument(final Document document, final LocalDateTime importTime, final LocalDateTime endTime, Boolean completed,
            final Integer entityType, final AppUser createdBy, final Integer totalRecords, final Integer successCount,
            final Integer failureCount) {
        this.document = document;
        this.importTime = Date.from(importTime.atZone(ZoneId.systemDefault()).toInstant());
        this.endTime = Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant());
        this.completed = completed;
        this.entityType = entityType;
        this.createdBy = createdBy;
        this.totalRecords = totalRecords;
        this.successCount = successCount;
        this.failureCount = failureCount;

    }

    public void update(final LocalDateTime endTime, final Integer successCount, final Integer errorCount) {
        this.endTime = Date.from(endTime.atZone(ZoneId.systemDefault()).toInstant());
        this.completed = Boolean.TRUE;
        this.successCount = successCount;
        this.failureCount = errorCount;
    }

    public Document getDocument() {
        return this.document;
    }

    public Integer getEntityType() {
        return this.entityType;
    }

}
