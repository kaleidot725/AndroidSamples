## Android の TextView の欧文フォントを指定した場合に日本語は表示できるのか検証する

## はじめに

Android では `TextView`  でテキストを表示しますが `TextView`  のフォントは`android:fontFamily` で変更できるようになっています。

```xml
<TextView
     android:layout_width="match_parent"
     android:layout_height="wrap_content"
     android:fontFamily="@font/font_file"
     android:textFontWeight="400"
     android:textSize="16sp"
     android:text="abcdefg1234567890"/>
```

例えば`android:fontFamily` に欧文フォントを指定したときに日本語を表示できるのか気になったので検証してみました。

## 欧文フォントを指定した場合に日本語を表示できるのか？

欧文フォントを指定したときに日本語を表示できるのか検証してみます。

今回は欧文フォント として Roboto を指定して日本語を表示してみようと思います。

Roboto は Google フォントからダウンロードできますが以下のような見た目です。

![2021-11-11 22.18.35 fonts.google.com d8486da49767.png](https://res.craft.do/user/full/3a21bd0e-fe7a-39aa-73ad-b52ef24b655b/doc/E377EE4D-D9D7-46F5-B3EC-182B5BF6AF16/C753BCA5-2997-41F6-9B6E-2E31AB0828A8_2/2021-11-11%2022.18.35%20fonts.google.com%20d8486da49767.png)

もちろん欧文フォントなのでRoboto フォントには日本語含まれていません。

例えば Google Font で日本語を表示しようとすると□(豆腐)が表示されます。

![2021-11-11 21.57.03 fonts.google.com 97144bd0247a.png](https://res.craft.do/user/full/3a21bd0e-fe7a-39aa-73ad-b52ef24b655b/doc/E377EE4D-D9D7-46F5-B3EC-182B5BF6AF16/8CF759BA-CCBF-4055-8441-C470F104BCFA_2/2021-11-11%2021.57.03%20fonts.google.com%2097144bd0247a.png)

Roboto フォントでアルファベットと日本語を表示する TextView で動作確認してみます。

```xml
<?xml version="1.0" encoding="utf-8"?>
<ScrollView xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent">
    <LinearLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:orientation="vertical"
        tools:context=".MainActivity">

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:text="Robotoを設定した場合"
            android:textColor="@color/purple_200"
            android:textSize="12sp" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:fontFamily="@font/roboto_medium"
            android:text="abcdefg1234567890"
            android:textFontWeight="400"
            android:textSize="16sp" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:fontFamily="@font/roboto_medium"
            android:text="あいうえおかきくけこさしすせそ"
            android:textFontWeight="400"
            android:textSize="16sp" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:fontFamily="@font/roboto_medium"
            android:text="abcdefg1234567890あいうえおかきくけこさしすせそ"
            android:textFontWeight="400"
            android:textSize="16sp" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:layout_marginTop="16dp"
            android:text="Robotoを設定し、Weightを800にした場合"
            android:textColor="@color/purple_200"
            android:textSize="12sp" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:fontFamily="@font/roboto_medium"
            android:text="abcdefg1234567890"
            android:textFontWeight="700"
            android:textSize="16sp" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:fontFamily="@font/roboto_medium"
            android:text="あいうえおかきくけこさしすせそ"
            android:textFontWeight="700"
            android:textSize="16sp" />

        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:fontFamily="@font/roboto_medium"
            android:text="abcdefg1234567890あいうえおかきくけこさしすせそ"
            android:textFontWeight="700"
            android:textSize="16sp" />

    </LinearLayout>
</ScrollView>
```

Roboto をフォントに設定していますが日本語が □（豆腐）にはならず問題なく表示されます。

![Image](https://res.craft.do/user/full/3a21bd0e-fe7a-39aa-73ad-b52ef24b655b/doc/E377EE4D-D9D7-46F5-B3EC-182B5BF6AF16/AA809988-A5C1-4053-A144-B9DADBF6E35C_2/Image)

どうやら Android では欧文フォントを設定したときに日本語が表示されても□（豆腐）にならないようによしなに制御してくれるようです。

## 日本語で表示されている部分は何フォントになるのか？

どうやら日本語で表示されている部分はシステムフォントが適用されるようです。

例えば Android 12 の日本語のシステムフォントは Noto SansCJK JP が採用されています。

```xml
// Android 12 のシステムフォントが定義されているソースコード
// https://cs.android.com/android/platform/superproject/+/master:frameworks/base/data/fonts/fonts.xml?q=fonts.xml&ss=android%2Fplatform%2Fsuperproject

    <family lang="ja">
        <font weight="400" style="normal" index="0" postScriptName="NotoSansCJKjp-Regular">
            NotoSansCJK-Regular.ttc
        </font>
        <font weight="400" style="normal" index="0" fallbackFor="serif"
              postScriptName="NotoSerifCJKjp-Regular">NotoSerifCJK-Regular.ttc
        </font>
    </family>
```

そのため欧文フォント(Roboto)を指定したときの日本語はNoto Sans CJK JP で表示されます。

![Image](https://res.craft.do/user/full/3a21bd0e-fe7a-39aa-73ad-b52ef24b655b/doc/E377EE4D-D9D7-46F5-B3EC-182B5BF6AF16/AA809988-A5C1-4053-A144-B9DADBF6E35C_2/Image)

本当にそうなっているか実際に表示された文字とフォントの文字を比較してみましたが一致したのでシステムフォントが日本語が表示されているようです。

![test.drawio.png](https://res.craft.do/user/full/3a21bd0e-fe7a-39aa-73ad-b52ef24b655b/doc/E377EE4D-D9D7-46F5-B3EC-182B5BF6AF16/EB550BF0-97ED-4E45-AC6E-16A14F6734B0_2/test.drawio.png)

## おわりに

Android の TextView の欧文フォントを指定した以下のような動作になる。

- 欧文フォントにはない文字（日本語など）はシステムフォントで表示される
- Android 12 では日本語のシステムフォントは Noto Sans CJK JP である。

## 参考文献

- [検証に使ったソースコード](https://github.com/kaleidot725-android/system_fonts)

