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

import java.io.File
import java.net.{InetAddress, URI}
import java.nio.file.{Path, Paths}
import java.util.{Locale, UUID}
import java.{lang => jl}
import scala.reflect.ClassTag

trait Converter[A, B] {

  def convert(a: A): Result[B]

  def map[C](f: B => C): Converter[A, C] =
    convert(_).map(f)

  def flatMap[C](f: B => Converter[A, C]): Converter[A, C] =
    a => convert(a).flatMap(f(_).convert(a))
}

object Converter {

  def apply[A, B](implicit C: Converter[A, B]): Converter[A, B] = C


  def from[A, B](f: A => Result[B]): Converter[A, B] =
    a => Result.Try(f(a)).flatten

  def fromTry[A, B](f: A => B): Converter[A, B] =
    a => Result.Try(f(a))


  type FromString[A] = Converter[String, A]

  object FromString {
    def apply[A](implicit A: FromString[A]): FromString[A] = A
  }


  private[this] final val _identity: Converter[Any, Any] =
    Result.successful(_)

  implicit def identityConverter[A]: Converter[A, A] =
    _identity.asInstanceOf[Converter[A, A]]


  implicit lazy val symbolFromString: FromString[Symbol] =
    fromTry(Symbol.apply)

  implicit def javaEnumFromString[A <: jl.Enum[A]](implicit A: ClassTag[A]): FromString[A] = {
    val enums = A.runtimeClass.asInstanceOf[Class[A]].getEnumConstants
    from { s =>
      enums.find(_.name() == s).fold(
        Result.failure[A](ConfigError(s"$s must be one of ${enums.mkString(", ")}")))(
        Result.successful)
    }
  }

  implicit lazy val uuidFromString: FromString[UUID] =
    fromTry(UUID.fromString)

  implicit lazy val localeFromString: FromString[Locale] =
    from { s =>
      Locale.getAvailableLocales.find(_.toString == s).fold(
        Result.failure[Locale](ConfigError(s"Locale '$s' is not available")))(
        Result.successful)
    }

  implicit lazy val pathFromString: FromString[Path] =
    fromTry(Paths.get(_))

  implicit lazy val fileFromString: FromString[File] =
    fromTry(new File(_))

  implicit lazy val inetAddressFromString: FromString[InetAddress] =
    fromTry(InetAddress.getByName)

  implicit lazy val uriFromString: FromString[URI] =
    fromTry(new URI(_))

}
