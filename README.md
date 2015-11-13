# RecyclerFragment
The recently introduced `RecyclerView` has changed the way of handling list of elements.

This library intends to leverage many of the powerful features that the `RecyclerView` provides by wrapping the `RecyclerView` into a `Fragment` with some predefined `Adapter` classes.

## Previews

![alt tag](art/single_choice.png)
![alt tag](art/default_section.png)
![alt tag](art/custom_section.png)

## Download
RecyclerFragment requires at minimum Android 2.1 (API 7).

Gradle:

``
compile 'biz.kasual:recyclerfragment:1.0.0'
``

Maven:

```
<dependency>
  <groupId>biz.kasual</groupId>
  <artifactId>recyclerfragment</artifactId>
  <version>1.0.0</version>
  <type>aar</type>
</dependency>
```

Eclipse: [recyclerfragment-1.0.0.aar](https://github.com/KasualBusiness/RecyclerFragment/releases/download/1.0.0/recyclerfragment-1.0.0.aar)

## Usage

### Basic usage

Create a layout for your fragment that will hold the `RecyclerView`

recycler_layout.xml:

```
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
              android:layout_width="match_parent"
              android:layout_height="match_parent">

    <android.support.v7.widget.RecyclerView
        android:id="@+id/recycler_view"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:scrollbars="vertical"/>

</FrameLayout>
```

Then create a `Fragment` that inherits from `RecyclerFragment<T>` where `T` is your model that will be used to data bind your views. You don't need to worry about implementing the `sortSectionMethod()` method for now as we will see this while displaying items inside sections.

```
public class YourRecyclerFragment extends RecyclerFragment<T> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.recycler_layout, container, false);
        return contentView;
    }

    @Override
    public String sortSectionMethod() {
        return null;
    }
}
```

Now create your `View` that will be handled by your `Adapter`. We provide a `DefaultCardView` class that serves as a pre-configured `CardView` and let you freely configure your layout without having to redefine the `CardView` as root. Using this is not mandatory and you can use any `ViewGroup` that fits your needs :

your_item_view.xml :

```
<?xml version="1.0" encoding="utf-8"?>
<merge xmlns:android="http://schemas.android.com/apk/res/android"> // In this case refer to DefaultCardView

    <RelativeLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content">

        // Your inner views

    </RelativeLayout>

</merge>
```

```
public class YourItemView extends DefaultCardView {

    public YourItemView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initViews(context);
    }

    public YourItemView(Context context) {
        super(context);
        initViews(context);
    }

    public void bind(T yourModel) {
        // Update your layout given the T model
    }

    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.sample_item_view, this);
        
        // Retrieve your inner views references
    }
}
```

After designing your item views you can create your `Adapter` that inherits from `RecyclerAdapter<T>` and implement the abstract methods that will be used to create/bind those item views inside your `RecyclerView`.

```
public class YourAdapter extends RecyclerAdapter<T> {

    public YourAdapter(Context context) {
        super(context);
    }

    @Override
    protected View onCreateItemView(ViewGroup parent, int viewType) {
        return new YourItemView(mContext);
    }

    @Override
    protected void onBindItemView(View v, int position) {
        YourItemView sampleItemView = (YourItemView)v;
        YourItemView.bind(getItemAt(position));
    }

}
```

You don't need to worry about the `ViewHolder` pattern, it is already taken care of inside the library so you just need to focus on your layouts without any performance loss.

Finally, in `YourRecyclerFragment`, call the `configureFragment`method with your `RecyclerView` reference from your layout and your `Adapter`. You will then be able to display your items. Your `Fragment` should look like :

```
public class YourRecyclerFragment extends RecyclerFragment<T> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.recycler_layout, container, false);

        RecyclerView recyclerView = (RecyclerView)contentView.findViewById(R.id.recycler_view);
        YourAdapter adapter = new YourAdapter(getActivity());

        configureFragment(recyclerView, adapter);
        
        List<T> yourItems = new ArrayList<>();
        
        // Fill in your array
        
        displayItems(yourItems);

        return contentView;
    }

    @Override
    public String sortSectionMethod() {
        return null;
    }
}
```

### Choice mode

![alt tag](art/single_choice.png)
![alt tag](art/multiple_choice.png)

The choice mode pattern is well-known for all developers that previously used the `ListView`. This pattern has not been implemented for `RecyclerView` as its logic differs from the original `ListView`. This library provides a way to configure your `Adapter` with a choice mode that can be either SINGLE_CHOICE or MULTIPLE_CHOICE. By default it is set to SINGLE_CHOICE. Simply call : 

```
yourAdapter.setChoiceMode(RecyclerAdapter.ChoiceMode.MULTIPLE_CHOICE);
```

You can retrieve your selected items with these following methods:

```
yourAdapter.isItemViewToggled(position);
yourAdapter.getSelectedItemViewCount();
```

If you need to manually select/deselect several items, use these methods:

```
yourAdapter.toggleItemView(position);
yourAdapter.clearSelectedItemViews();
```

## License

```
Copyright 2015 Kasual Business.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
