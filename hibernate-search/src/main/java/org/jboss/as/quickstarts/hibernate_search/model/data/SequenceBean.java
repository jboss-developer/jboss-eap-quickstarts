/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.hibernate_search.model.data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User: tharinduj This is the sequence generating table that is required for
 * auto increment for table column Required for MySQL with Hibernate to do this
 * way
 */
@Entity
@Table(name = "SEQUENCE_TABLE")
public class SequenceBean {

	private String sequenceName;
	private int sequenceCount;

	public SequenceBean(String sequenceName, int sequenceCount) {
		this.sequenceName = sequenceName;
		this.sequenceCount = sequenceCount;
	}

	@Id
	@Column(name = "SEQ_NAME")
	public String getSequenceName() {
		return sequenceName;
	}

	public void setSequenceName(String sequenceName) {
		this.sequenceName = sequenceName;
	}

	@Column(name = "SEQ_COUNT")
	public int getSequenceCount() {
		return sequenceCount;
	}

	public void setSequenceCount(int sequenceCount) {
		this.sequenceCount = sequenceCount;
	}
}
