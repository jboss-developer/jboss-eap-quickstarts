/*
 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
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
package org.quickstarts.kitchensink.data;

import org.quickstarts.kitchensink.model.Member;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * MongoDB repository for Member documents.
 * Migrated from JPA EntityManager to Spring Data MongoDB.
 * Spring Data automatically implements basic CRUD operations and custom query methods.
 */
public interface MemberRepository extends MongoRepository<Member, String> {

    /**
     * Find a member by email address.
     * Spring Data MongoDB automatically implements this based on method naming convention.
     *
     * @param email The email to search for
     * @return The member with the given email, or null if not found
     */
    Member findByEmail(String email);

    /**
     * Find all members ordered by name in ascending order.
     * Spring Data MongoDB automatically implements this based on method naming convention.
     *
     * @return List of all members sorted by name
     */
    List<Member> findAllByOrderByNameAsc();

    /**
     * Find a member by ID, returning Member or null.
     * Helper method for backward compatibility with existing code.
     *
     * @param id The member ID
     * @return The member with the given ID, or null if not found
     */
    default Member findMemberById(String id) {
        return findById(id).orElse(null);
    }
}
