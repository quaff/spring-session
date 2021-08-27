/*
 * Copyright 2018-2021 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.session.data.mongo.integration;

import java.io.IOException;

import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import de.flapdoodle.embed.mongo.MongodExecutable;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.session.data.mongo.ReactiveMongoSessionRepository;
import org.springframework.session.data.mongo.config.annotation.web.reactive.EnableMongoWebSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.util.SocketUtils;

/**
 * @author Greg Turnquist
 */
@ContextConfiguration
public class ReactiveConfigurationTest extends AbstractClassLoaderTest<ReactiveMongoSessionRepository> {

	@Configuration
	@EnableMongoWebSession
	static class Config {

		private int embeddedMongoPort = SocketUtils.findAvailableTcpPort();

		@Bean(initMethod = "start", destroyMethod = "stop")
		MongodExecutable embeddedMongoServer() throws IOException {
			return MongoITestUtils.embeddedMongoServer(this.embeddedMongoPort);
		}

		@Bean
		@DependsOn("embeddedMongoServer")
		ReactiveMongoOperations mongoOperations() {

			MongoClient mongo = MongoClients.create("mongodb://localhost:" + this.embeddedMongoPort);
			return new ReactiveMongoTemplate(mongo, "test");
		}

	}

}
