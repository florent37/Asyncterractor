# Asyncterractor

Transform any object into an async object (can be useful for VIPER)

Add `@OnThread` before your class

```
@OnThread
public class MainPresenterImpl implements MainPresenter {

    @Override
    public void start() {
        //some code
    }

}
```

Then use it wrapped by `Asyncterractor.of`

```
MainPresenter mainPresenter = Asyncterractor.of(new MainPresenterImpl());

mainPresenter.start(); //will be executed in a different thread
```

# Android Main Thread

Add `@OnUiThread` before your class

```
@OnUiThread
public interface MainView {
    void displayUserName(String userName);

    void displayMessage(String message);
}
```

Then use it wrapped by `Asyncterractor.of`

```
this.mainView = Asyncterractor.of(this.mainView);

mainView.displayUserName("florent"); //will be executed in UI thread
```

# Download

<a href='https://ko-fi.com/A160LCC' target='_blank'><img height='36' style='border:0px;height:36px;' src='https://az743702.vo.msecnd.net/cdn/kofi1.png?v=0' border='0' alt='Buy Me a Coffee at ko-fi.com' /></a>

In your module [![Download](https://api.bintray.com/packages/florent37/maven/asyncterractor-compiler/images/download.svg)](https://bintray.com/florent37/maven/asyncterractor-compiler/_latestVersion)
```groovy
compile 'com.github.florent37:asyncterractor:1.0.0'
annotationProcessor 'com.github.florent37:asyncterractor-compiler:1.0.0'
```

# Credits

Author: Florent Champigny

<a href="https://plus.google.com/+florentchampigny">
  <img alt="Follow me on Google+"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/gplus.png" />
</a>
<a href="https://twitter.com/florent_champ">
  <img alt="Follow me on Twitter"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/twitter.png" />
</a>
<a href="https://fr.linkedin.com/in/florentchampigny">
  <img alt="Follow me on LinkedIn"
       src="https://raw.githubusercontent.com/florent37/DaVinci/master/mobile/src/main/res/drawable-hdpi/linkedin.png" />
</a>

# License

    Copyright 2017 florent37, Inc.

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
