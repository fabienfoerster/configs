/*
 * Copyright 2013-2016 Tsukasa Kitachi
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package configs

import com.typesafe.config.Config

package object syntax {

  @deprecated("import configs.syntax.exception._ instead", "0.4.0")
  implicit class ConfigOps(private val self: Config) extends AnyVal {

    @deprecated("import configs.syntax.exception._ instead", "0.4.0")
    def extract[A: Configs]: A =
      exception.ConfigOps(self).extract[A]

    @deprecated("import configs.syntax.exception._ instead", "0.4.0")
    def get[A: Configs](path: String): A =
      exception.ConfigOps(self).get(path)

    @deprecated("import configs.syntax.exception._ instead", "0.4.0")
    def getOpt[A: Configs](path: String): Option[A] =
      exception.ConfigOps(self).getOpt(path)

    @deprecated("import configs.syntax.exception._ instead", "0.4.0")
    def getOrElse[A: Configs](path: String, default: => A): A =
      exception.ConfigOps(self).getOrElse(path, default)

  }

}