/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to you under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.logging.log4j.mongodb3;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoIterable;
import org.apache.commons.lang3.JavaVersion;
import org.apache.commons.lang3.SystemUtils;
import org.apache.logging.log4j.core.test.AvailablePortSystemPropertyTestRule;
import org.apache.logging.log4j.core.test.RuleChainFactory;
import org.apache.logging.log4j.mongodb3.MongoDb3TestRule.LoggingTarget;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.rules.RuleChain;

/**
 * Tests MongoDbRule.
 * <p>
 * The test framework {@code de.flapdoodle.embed.mongo} requires Java 8.
 * </p>
 */
public class MongoDb3TestTestRuleTest {

    private static final AvailablePortSystemPropertyTestRule mongoDbPortTestRule = AvailablePortSystemPropertyTestRule
            .create(MongoDb3TestConstants.SYS_PROP_NAME_PORT);

    private static final MongoDb3TestRule mongoDbTestRule = new MongoDb3TestRule(mongoDbPortTestRule.getName(),
            MongoDb3TestTestRuleTest.class, LoggingTarget.NULL);

    @ClassRule
    public static RuleChain mongoDbChain = RuleChainFactory.create(mongoDbPortTestRule, mongoDbTestRule);

    @BeforeClass
    public static void beforeClass() {
        Assume.assumeTrue(SystemUtils.isJavaVersionAtLeast(JavaVersion.JAVA_1_8));
    }

    @Test
    public void testAccess() {
        @SuppressWarnings("resource") // Mongo client is managed by the test rule.
        final MongoIterable<String> databaseNames = mongoDbTestRule.getMongoClient().listDatabaseNames();
        Assert.assertNotNull(databaseNames);
        Assert.assertNotNull(databaseNames.first());
    }

    @Test
    public void testMongoDbTestRule() {
        Assert.assertNotNull(mongoDbTestRule);
        Assert.assertNotNull(mongoDbTestRule.getStarter());
        @SuppressWarnings("resource") // Mongo client is managed by the test rule.
        final MongoClient mongoClient = mongoDbTestRule.getMongoClient();
        Assert.assertNotNull(mongoClient);
        Assert.assertNotNull(mongoDbTestRule.getMongodExecutable());
        Assert.assertNotNull(mongoDbTestRule.getMongodProcess());
    }
}
