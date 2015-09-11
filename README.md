#Typesafe Query

Typesafe QueryはSQLを安全に記述するためのとても便利な一連のヘルパを提供します。  

##Typesafe Queryとは？


###特徴
Typesafe Queryでは以下を重要視しています。

* 柔軟なO/Rマッピング
* JDBCを(なるべく)意識させない
* ネイティブSQLを記述するようにJavaコードを記述できる
* スキーマの変更に強い

###Java8対応
Typesafe QueryではJava8から導入されたOptional、DateTime APIをO/Rマッピングに使用できます。

##インストール
Maven

```
<dependency>
  <groupId>com.github.typesafe_query</groupId>
  <artifactId>typesafe-query-core</artifactId>
  <version>0.0.2</version>
</dependency>
```

##使い方

###データベースコネクションの登録

Typesafe Queryの各クラスは`ConnectionHolder`クラスを通じてデータベースコネクションを取得します。  
`ConnectionHolder`クラスはスレッド毎にコネクションを保持する仕組みですので、スレッド毎に同じコネクションが使用されます。  
登録は以下のようにします。

```
ConnectionHolder.getInstance().set(connection);
```

この処理は通常共通化しておくべきです。

###テーブルエンティティの作成
Typesafe Queryではアノテーションを使用してO/Rマッピング用エンティティクラスを作成することで、タイプセーフなクエリを実現するためのエンティティメタデータクラスを自動的に作成します。  

####プロパティを定義する
テーブルのレコードをマッピングするためのクラスを作成します。これは通常のJavaBeanでかまいませんが、２つだけ注意があります。

 - **NULL許可項目はOptionalで定義**
 - **日付系は`LocalDate` `LocalTime` `LocalDateTime`で定義**

これらに注意してプロパティを定義していってください。

####アノテーションを付与する
#####@Model

エンティティクラスに注釈します。  
このアノテーションがあることでエンティティメタデータクラスが自動的に作成されます。

```
@Model
public class Customer{
```

テーブル名はエンティティクラス名と認識されます。キャメルケースで命名されたクラスはスネークケースに変換した名前をテーブル名とします。

```
Customer -> customer
UserInfo -> user_info
```

#####@Table

クラス名とテーブル名が異なる場合に使用します。  
例えばすべてのテーブルに特定の接頭辞があり、Javaクラスでは接頭辞を除いた命名にしたい等の場合にこのアノテーションを付与します。

```
@Model
@Table(name="M_CUSTOMER")
public class Customer{
```

#####@Id

プライマリーキー項目に注釈します。  

```
@Id
private Integer id;
```

これは単一のプライマリーキーの指定に使用します。複合プライマリーキーの場合は後述する`@EmbeddedId`を使用して下さい。

**注意**  
エンティティクラスには`@Id`または`@EmbeddedId`のどちらかを必ず指定する必要があります。


#####@EmbeddedId

複合プライマリーキーの指定に使用します。

```
public class CartPK{
  @Column("CUST_ID")
  private Integer customerId;
  private Integer no;
  //getters and setters
}

@Model
public class Cart{
  @EmbeddedId
  private CartPK cartPK;
}

```


#####@Column

プロパティ名とカラム名が異なる場合に使用します。`@Table`と同様、別名をつけることができます。

```
@Column(name="USR_NM")
private String userName;
```

#####@Transient

O/Rマッピングの対象にしたくないプロパティに注釈します。

```
@Transient
private String javaField;
```

また、Typesafe Queryでは以下で修飾されたフィールドは無視されます。

 - static
 - transient
 - native
 - violate


###エンティティメタデータクラス
Typesafe Queryではエンティティクラスのコンパイル時にアノテーションプロセッサにより自動的にエンティティメタデータクラスを作成します。  
メタデータクラスはエンティティクラス名にアンダースコア`_`をつけたものがクラス名になります。  

    @Model
    public class Customer {
      …
    }

例えば、上記のようなエンティティクラスの場合、次のようなメタデータクラスが自動生成されます。

    public final class Customer_ {
      …
    }

メタデータクラス名は`Customer_`になります。  
メタデータクラスはタイプセーフなクエリを実現するための重要なクラスです。このメタデータクラスを駆使してタイプセーフなクエリを作成していきましょう。


###データの基本操作
データの基本操作は`ModelHandler`オブジェクトを介して行います。  
データベース操作の`INSERT`、`UPDATE`、`DELETE`などのデータ更新操作はすべて`ModelHandler`クラスを介して行われます。  
`ModelHandler`オブジェクトはエンティティメタデータクラスのメソッド`model()`として定義されています。


例えば次の例を見てください。  

    @Model
    public class Customer{
      @Id
      private Long id;
      private String name;
      …
    }

    Customer c = new Customer();
    c.setName("HOGE");
    Customer_.model().create(c); //insert

    c.setName("PIYO");
    Customer_.model().save(c); //update

    Customer_.model().delete(c); //delete

`ModelHandler`ヘルパクラスを使用することでSQLを意識せず直感的にコーディングを行うことができます。


###データの検索
Typesafe Queryではエンティティメタデータクラスと`Finder`オブジェクトを使用してデータを検索します。  
`Finder`オブジェクトはエンティティメタデータクラスのメソッド`find()`として定義されています。`Finder`の代表的な検索方法は以下のとおりです。

####IDで検索
データを検索する最も簡単な方法です。プライマリーキーで検索するため高速です。

    Optional<Customer> c = Customer_.find().byId(10L);

複合Keyの場合はキークラスを指定して検索できます。

    ChildKey ck = new ChildKey();
    ck.setParentId(10L);
    ck.setChildId(1L);

    Optional<Child> c = Child_.find().byId(ck);


####全件検索

    List<Customer> customers = Customer_.find().list();

これは**すべての**データを取得します。データ件数が明らかに多い場合、この検索を行うとパフォーマンスの低下を招く恐れがあるため、通常は次のように件数を指定します。

    //取得件数を指定
    List<Customer> customers = Customer_.find().list(10);

    //オフセットと取得件数を指定
    List<Customer> customers = Customer_.find().list(50,10);

####シンプルな条件を指定して検索
1つのテーブルに対していくつかの条件を指定して検索を行うことは頻繁にあるでしょう。Typesafe Queryではこれらの検索でも文字ベースのSQLをわざわざ記述することはありません。


    // where customer.age < 30
    List<Customer> customers = Customer_.find().listWhere(
       Customer_.AGE.lt(30)
    );

エンティティメタデータのプロパティを使用して条件を指定します。複数の条件を指定することもできます。

    import static com.github.typesafe_query.Q.*;

    ...

    List<Customer> customers = Customer_.find().listWhere(
       and(
         Customer_.AGE.lt(30),
         Customer_.COUNTRY.eq("Japan")
       )
    );

この検索方法は**特に重要**です。  
一般的なWebアプリケーションの開発ではこの検索方法とIDでの検索がほとんどでしょう。複雑なクエリを設計する前にまずはシンプルな条件で検索できないか検討してください。

####動的検索条件
UIで検索条件を指定させて検索処理を行う機能は、一般的なアプリケーションなら頻繁にあるでしょう。しかしながら検索条件が多くなるとプログラマはチェック処理の`if`地獄に陥りがちです。  
Typesafe Queryのタイプセーフな条件式では、条件の値が`null`のものは無視して条件に追加しません。

    Condition c = ...
    List<Customer> customers = Customer_.find().listWhere(
       and(
         Customer_.AGE.lt(c.getAge()), // null
         Customer_.COUNTRY.eq(c.getCountry()) // "Japan"
       )
    );

    //where customer.country = 'Japan'

上記のように、条件式の値が`null`の場合は無視されます。つまりプログラマは必須の条件のみチェックすればよいことになり、シンプルでわかりやすいコードを記述することができます。  
もし明示的に`NULL`の条件を追加したい場合は`Customer_.AGE.isNull()`としてください。

####Qクラス
`Q`クラスをstatic importすることでand条件やexists式、SQL関数などを使うことができます。また、後述するクエリの作成のためのヘルパメソッドを利用できます。  
ソースコードの可読性向上のために、エンティティを検索するクラスでは以下のように常にstatic importしておくことをおすすめします。

    import static com.github.typesafe_query.Q.*;


`Q`クラスの詳細はJavadocを参照してください。

####タイプセーフクエリを組み立てて検索
取得順序を指定したり1つのテーブルでは完結しないような複雑なクエリを組み立てることができます。  
ただし、複雑なクエリは間違いを犯す可能性が高くバグ発生の元になります。複雑なクエリを組み立てる前にIDで取得できないか、シンプルな条件指定検索で仕様を満たすことができないかをもう一度考えてみましょう。

    List<Parent> parents = select()
		.distinct()
		.from(Parent_.TABLE)
		.outerJoin(Parent_.CHILD_MODELS)
		.where(
			and(
				Child_.NAME.in("JACKSON","HOGEO"),
				or(
					Parent_.AGE.lt(18),
					Parent_.AGE.gt(30),
				),
				exists(subQuery()
					.from(Parent_.TABLE,"p2")
					.where(Parent_.ID.eq($("p2",Parent_.ID)))
				)
			)
		).orderBy(Parent_.NAME.asc())
		.forOnce()
		.getResultList(Parent.class);

Typesafe Queryではエンティティメタデータ、クエリヘルパーを使用して上記のようなSQLライクなタイプセーフクエリを組み立てることができます。  
タイプセーフであるということは、変更をコンパイルエラーという形で知ることができるということですから、SQLを文字列で記述するより安全です。  

*Hint*
以下にタイプセーフクエリを選択するべきケースの例を示します。

* order by句で並べ替えをしたい
* 重複を排除したい
* 複数のテーブルの内容をフラットな結果で取得したい
* 集計関数を使用して集計をしたい

詳しくはメタデータ、`Finder`のJavadocを参照してください。


#####パラメータを付与する
タイプセーフクエリにはパラメータを埋め込むことができます。ループ中で複数回呼び出すなど、クエリを使いまわしたい場合などに有効です。

クエリの条件式にパラメータを埋め込みます。

    Customer_.NAME.like(param());


動的に値をセットするには、作成したクエリにパラメータの値をセットします。

    query.addParam("%HOGE%");


**注意**  
パラメータは最終的に組み立てられたSQLの登場順に追加されます。複雑なSQLでは意図しない順番でパラメータが登録される恐れがありますのでログに出力されたSQLを確認するなどして十分にテストを実施してください。


####1行のみ取得する
これまで説明してきた`getResultList`のメソッドには同様の動作をする`getResult`というメソッドも存在します。これは**1行のみ取得できることが保証されている場合**に使用することができます。

```
Optional<Customer> c = 
  select()
  .from(Customer_.TABLE)
  .forOnce()
  .getResult(Customer.class);
```

もしデータが存在しない場合は`Optional.empty()`を返します。  

また、必ず行が取得できることがわかっている場合は`getRequiredResult`メソッドを使用することで`Optional`ではなく直接エンティティクラスを取得することができます。**行が取得できなかった場合には例外がスローされます**。


```
Customer c = 
  select()
  .from(Customer_.TABLE)
  .forOnce()
  .getRequiredResult(Customer.class);
```


###一括更新、一括削除
`Bulk`クラスを使用して一括更新、一括削除をすることができます。  
`Bulk`オブジェクトはエンティティメタデータクラスのメソッド`bulk()`として定義されています。

####一括更新
```
int count = ApUser_.bulk().updateWhere(
  set(
    ApUser_.LOCK_FLG.eq("0")
  ),
  ApUser_.UNIT_ID.eq("U1")
);
//update apuser set lock_flg ='0' where unit_id = 'U1'
```

####一括削除
```
int count = ApUser_.bulk().deleteWhere(ApUser_.UNIT_ID.eq("U1"));
//delete from apuser where unit_id = 'U1'
```

###ファイルに記述されたSQLを実行する

これは上記のどの検索方法でも実現できない場合の**最終手段**です。この方法に行き着いた場合はまず深呼吸をして、もう一度上記のいずれかの方法で実現できないか考えなおしましょう。  
nativeSQLはタイプセーフではないため変更に対して安全ではありません。パフォーマンス・チューニングの結果この方法にたどりついた場合を除いてこの方法は**極力使用すべきではありません**。

```
List<User> result = namedQuery("/S001.sql")
  .addParam("HOGE")
  .forOnce()
  .getResultList(User.class);
```

クラスパスルートをあらわす`/`からファイルが存在する位置までのパスを記述することでTypesafe Queryはファイル内のSQLを実行します。１つのファイルには１つのSQLを記述してください。
