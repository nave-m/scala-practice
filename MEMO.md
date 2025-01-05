
# Scalaの言語仕様

## Trait

https://docs.scala-lang.org/ja/tour/traits.html

> Traits are used to share interfaces and fields between classes. They are similar to Java 8’s interfaces. Classes and objects can extend traits, but traits cannot be instantiated and therefore have no parameters.

クリーンアーキテクチャでユースケースを抽象に依存させるためにService,Query,Repositoryを定義するときはこれ

## Singleton

https://docs.scala-lang.org/tour/singleton-objects.html

> static members in Java are modeled as ordinary members of a companion object in Scala.

```scala
object Class {
  // 関数
  // フィールド
}
```

## Case class 

https://docs.scala-lang.org/tour/case-classes.html

イミュータブルで、等価判定が値で行われる
SwiftのstructやKotlinのdata class同様

# sbt

## 依存関係の記述

https://www.scala-sbt.org/1.x/docs/Multi-Project.html#Dependencies

- aggregate
    - https://www.scala-sbt.org/1.x/docs/Multi-Project.html#Aggregation
    - 関連するプロジェクトに対してsbtのタスクを実行する
- classpath (dependsOn)
    - https://www.scala-sbt.org/1.x/docs/Multi-Project.html#Classpath+dependencies
    - クラスパス依存性(他のプロジェクトのコードを参照するための指定)
        - プロジェクトだけ指定すると暗黙的にsbtのcompileコンフィグレーションの依存性が定義される
        - `src/main` のコードを他のプロジェクトから使える
    - `dependsOn({プロジェクト名} % "test->test;compile->compile")`のように明示的にtestコンフィグレーションの依存性を書くと
      `src/test` のコードを他のプロジェクトから使える

