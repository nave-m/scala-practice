
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

# zio

> The ZIO data type is called a functional effect, and represents a unit of computation inside a ZIO application. Similar to a blueprint or a workflow, functional effects are precise plans that describe a computation or interaction. When executed by the ZIO runtime system, a functional effect will either fail with some type of error, or succeed with some type of value.

ZIOデータ型は関数型プログラミングの作用であり、ZIOアプリケーション内の処理単位。
ZIOランタイムによって実行されると、ScalaのEither同様に失敗もしくは成功のデータが得られる。

> The ZIO data type has three type parameters: ZIO[R, E, A].

> Although this analogy is not precise, a ZIO effect can be thought of as a function:
> ```R => Either[E, A]```
> This function requires an R and produces a failure of type E or a success value of type A.

- RはZIOアプリケーションがZIO型を処理するにあたって必要な環境
    - Rが満たされないコードはコンパイルエラーになる
- 結果がFutureを返すものであっても、`ZIO[R,E,A]`で書ける

> UIO[A] — A type alias for ZIO[Any, Nothing, A], representing an effect that has no requirements, cannot fail, and can succeed with an A.
> URIO[R, A] — A type alias for ZIO[R, Nothing, A], representing an effect that requires an R, cannot fail, and can succeed with an A.
> Task[A] — A type alias for ZIO[Any, Throwable, A], representing an effect that has no requirements, may fail with a Throwable value, or succeed with an A.
> RIO[R, A] — A type alias for ZIO[R, Throwable, A], representing an effect that requires an R, may fail with a Throwable value, or succeed with an A.
> IO[E, A] — A type alias for ZIO[Any, E, A], representing an effect that has no requirements, may fail with an E, or succeed with an A.

ZIOのRやEを省略するパターンについては別名がある