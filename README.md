# JetTodoApp

Udemy参考教材: [【Android開発/2023年版】３つのアプリを作りながらJetpack Composeでのアプリ開発の基礎をマスター](https://www.udemy.com/course/android_jetpack_compose_beginner/)

アプリ概要: TODOアプリ

環境: Android Studio Electric Eel | 2022.1.1 2023 年 1 月 12 日

## レクチャーについて

流れ：

1. RoomでDB周りを作成
2. Hiltで依存関係注入 
3. UI, ViewModelの作成


## 学習ファイル

## 実装Memo

データベース周りのセット:

- Roomの依存関係を[`build.gradle(app)`](app/build.gradle)に追加
- Taskエンティティを作る
- TaskDaoを作る（インターフェース）
- DAOメソッドの非同期化
- データベースクラスの作成(`AppDataBase`)

依存関係注入:

- Hiltライブラリの[依存関係を追加する](https://developer.android.com/training/dependency-injection/hilt-android?hl=ja#groovy)
- Hiltモジュールの作成（[`Module`](app/src/main/java/com/example/jettodoapp/Module.kt)）
- Hiltアプリケーション、エントリーポイントの設置

### エラー等詰まったところ

「39.データベースクラスの作成」でビルドエラーの解決方法。`build.gradle(app)`のsdkバージョンを33->34に変更

## 学習Memo

Roomの主要コンポーネント: 

1. データベースクラス: SQLとのメインアクセスポイントになる
2. データエンティティ: テーブルに対するクラスを作成する。Todoアプリのタスクを保存する。
3. DAO: データベースクラスからこのインスタンスが提供される。その中にタスクのCRUDのメソッドを定義しておいて使う

DAOに定義したメソッドを非同期化する理由:

- UIスレッド（MainThread）をブロックしないようにする
  - MainThreadでDB操作やHTTP通信など時間のかかる処理を実行するとアプリが重くなる
- Room自体がMainThreadでのクエリ実行を許可していない

非同期化の方法:

| 方法                  | 内容                                                        | 実装方法    | 具体的な方法          |
|-----------------------|-------------------------------------------------------------|-------------|-----------------|
| 非同期ワンショットクエリ | 一回だけクエリが発行され、DBのスナップショットを返す         | coroutines  | `suspend`修飾子を使う |
| オブザーバブルクエリ   | テーブルに変更があるたびに、新しいデータを取得しにいくクエリ | Flow        | 戻り値を`Flow`にする   |


TaskDaoの実装方針:

- insert: 非同期ワンショットクエリ
- load: オブザーバブルクエリ
- update: 非同期ワンショットクエリ
- delete: 非同期ワンショットクエリ

データベースクラスが満たすべき要件:

- `@Database`アノテーション
- `RoomDatabase`クラスを拡張する抽象クラスであること
- 引数ゼロでDAOクラスのインスタンスを返す抽象メソッドを定義すること

### 依存関係注入

このTodoアプリでどのようにDIするか:

- `MainActivity` <- `MainViewModel`
- `MainViewModel` <- `TaskDao`

Hiltとは:

- 依存関係インジェクションをするための推奨ライブラリ

Hiltモジュールを作る理由:

- 後に`TaskDao`のインスタンスを`MainViewModel`にインジェクトしたい
  - しかし、TaskDaoはインターフェースでコンストラクタがない
    - そのため、Hiltにインスタンスの生成方法を教える必要がある

参照: [Hiltモジュール](https://developer.android.com/training/dependency-injection/hilt-android?hl=ja#hilt-modules)



### まとめ

- 時間がかかるような処理はMainThreadをブロックしないように非同期化する

## 参考サイト

[Roomについて](https://developer.android.com/training/data-storage/room?hl=ja)