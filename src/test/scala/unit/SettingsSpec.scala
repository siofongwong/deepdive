package org.deepdive.test.unit

import com.typesafe.config._
import org.deepdive.settings._
import org.deepdive.context._
import org.scalatest._

class SettingsSpec extends FunSpec {

  describe("Settings") {

    it("should parse a simple configuration file") {
      val settings = Settings.loadFromConfig(ConfigFactory.load("simple_config"))

      assert(settings.connection == Connection("localhost", 5432, "deepdive_test", 
        "root", "password"))
      
      assert(settings.relations.toSet == Set(
        Relation(
          "documents", 
          Map[String,String]("id" -> "Integer", "text" -> "Text", "meta" -> "Text"), 
          List[ForeignKey](ForeignKey("documents","id","documents","id")),
          None),
        Relation("entities", 
          Map[String,String]("id" -> "Integer", "document_id" -> "Integer", 
            "name" -> "String", "meta" -> "Text"), 
          List[ForeignKey](ForeignKey("entities","id","entities","id")),
          None)
      ))

      assert(settings.extractors == List(
        Extractor("extractor1", "entities", "SELECT * FROM documents", "udf/entities.py", 
          Option(FactorDesc("Entities", ImplyFactorFunction("id", Nil), UnknownFactorWeight(Nil))))
      ))

    }

  }

}