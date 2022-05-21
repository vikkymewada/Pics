package com.vikky.pics.ui.detail


import android.content.Intent
import android.graphics.RenderEffect
import android.graphics.Shader
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.snackbar.Snackbar
import com.jsibbold.zoomage.ZoomageView
import com.vikky.pics.Constants
import com.vikky.pics.R
import com.vikky.pics.data.model.Photo

import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailActivity : AppCompatActivity(), DetailViewModel.DetailNavigator {

    private lateinit var photo: Photo
    private lateinit var imgView: ZoomageView
    private lateinit var coordinator: CoordinatorLayout
    private val viewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_detail)
        viewModel.setNavigator(this@DetailActivity)
        val bundle = intent.extras
        if (bundle != null) {
            val photoItem: Photo? = bundle.getParcelable(Constants.SHARED_ELEMENT_TAG)
            if (photoItem == null) {
                onBackPressed()
                return
            }
            photo = photoItem
        }
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        coordinator = findViewById(R.id.coordinator)
        imgView = findViewById(R.id.zoomImgView)

        supportPostponeEnterTransition()
        imgView.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    imgView.viewTreeObserver.removeOnPreDrawListener(this)
                    supportStartPostponedEnterTransition()
                    return true
                }
            }
        )
        setupUI()
    }

    private fun setupUI() {
        findViewById<TextView>(R.id.author).text = "by " + photo.author

        val photoUrl = Constants.getPhotoUrl(photo)
        Glide.with(this@DetailActivity)
            .load(photo.download_url)
            .thumbnail(Glide.with(this@DetailActivity).load(photoUrl))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imgView)

        val bgImage = findViewById<ImageView>(R.id.bgImage)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            bgImage.setRenderEffect(
                RenderEffect.createBlurEffect(
                    50.0f,
                    50.0f,
                    Shader.TileMode.CLAMP
                )
            )
        }

        Glide.with(this@DetailActivity)
            .load(photo.download_url)
            .thumbnail(Glide.with(this@DetailActivity).load(photoUrl))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(bgImage)
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.details, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share -> {
                viewModel.handleShare()
                return true
            }
            R.id.save -> {
                viewModel.handleDownload(photo)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun share() {
        startActivity(Intent.createChooser(Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, photo.url)
            type = "text/plain"
        }, getString(R.string.share_photo)))
    }

    override fun error(resId: Int) {
        Snackbar.make(coordinator, resId, Snackbar.LENGTH_SHORT).show()
    }

    override fun openIntent(intent: Intent) {
        startActivity(intent)
    }
}