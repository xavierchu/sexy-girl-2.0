/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package org.meepo.sexygirl;

import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public abstract class BaseActivity extends Activity {

	protected ImageLoader imageLoader = ImageLoader.getInstance();

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.item_jingyan:
                switchContent(ImageUrlsFinder.IMAGETYPE.XINGAN);
				return true;
			case R.id.item_qingliang:
                switchContent(ImageUrlsFinder.IMAGETYPE.QINGLIANG);
				return true;
            case R.id.item_wenyi:
                switchContent(ImageUrlsFinder.IMAGETYPE.WENYI);
                return true;
            case R.id.item_suren:
                switchContent(ImageUrlsFinder.IMAGETYPE.SUREN);
                return true;
			default:
				return false;
		}
	}

    public abstract void switchContent(ImageUrlsFinder.IMAGETYPE type);

}
