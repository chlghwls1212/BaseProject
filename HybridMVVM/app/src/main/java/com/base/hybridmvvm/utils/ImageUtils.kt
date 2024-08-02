package com.base.hybridmvvm.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Base64
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

object ImageUtils {

    /**
     * uriToBitmap
     * uri 에서 Bitmap 으로 타입 캐스팅
     * @param context
     * @param uri
     * @return
     */
    fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
        return try {
            MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * uriToFile
     * uri 에서 File 으로 타입 캐스팅
     * @param context
     * @param uri
     * @param destinationFile
     * @return
     */
    fun uriToFile(context: Context, uri: Uri, destinationFile: File): File? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(destinationFile)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()
            destinationFile
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * uriToByteArray
     * uri 에서 byteArray 으로 타입 캐스팅
     * @param context
     * @param uri
     * @return
     */
    fun uriToByteArray(context: Context, uri: Uri): ByteArray? {
        return try {
            val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
            inputStream?.readBytes()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * bitmapToUri
     * bitmap 형식에서 Uri 로 타입 캐스팅
     * @param context
     * @param bitmap
     * @return
     */
    fun bitmapToUri(context: Context, bitmap: Bitmap): Uri? {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path =
            MediaStore.Images.Media.insertImage(context.contentResolver, bitmap, "Title", null)
        return Uri.parse(path)
    }

    /**
     * bitmapToFile
     * bitmap 형식에서 File 로 타입 캐스팅
     * @param bitmap
     * @param destinationFile
     * @return
     */
    fun bitmapToFile(bitmap: Bitmap, destinationFile: File): File? {
        return try {
            val outputStream = FileOutputStream(destinationFile)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            destinationFile
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * bitmapToByteArray
     * bitmap 형식에서 ByteArray 로 타입 캐스팅
     * @param context
     * @param ByteArray
     * @param destinationFile
     * @return
     */
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
        return byteArrayOutputStream.toByteArray()
    }

    /**
     * fileToUri
     * File 형식에서 Uri 로 타입 캐스팅
     * @param file
     * @return
     */
    fun fileToUri(file: File): Uri {
        return Uri.fromFile(file)
    }

    /**
     * fileToBitmap
     * File 형식에서 Bitmap 로 타입 캐스팅
     * @param file
     * @return
     */
    fun fileToBitmap(file: File): Bitmap? {
        return BitmapFactory.decodeFile(file.absolutePath)
    }

    /**
     * fileToByteArray
     * File 형식에서 ByteArray 로 타입 캐스팅
     * @param file
     * @return
     */
    fun fileToByteArray(file: File): ByteArray? {
        return try {
            file.readBytes()
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * byteArrayToBitmap
     * ByteArray 형식에서 Bitmap 으로 타입 캐스팅
     * @param byteArray
     * @return
     */
    fun byteArrayToBitmap(byteArray: ByteArray): Bitmap? {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    /**
     * byteArrayToFile
     * ByteArray 형식에서 File 으로 타입 캐스팅
     * @param byteArray
     * @param destinationFile
     * @return
     */
    fun byteArrayToFile(byteArray: ByteArray, destinationFile: File): File? {
        return try {
            val outputStream = FileOutputStream(destinationFile)
            outputStream.write(byteArray)
            outputStream.close()
            destinationFile
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * bitmapToBase64
     * 비트맵 이미지 Base64 인코딩
     * @param bitmap
     * @return
     */
     fun bitmapToBase64(bitmap: Bitmap?): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    /**
     * resizeBitmap
     * 이미지 사이즈 수정
     * @param bitmap
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        var width = bitmap.width
        var height = bitmap.height

        if (width > maxWidth || height > maxHeight) {
            val ratioBitmap = width.toFloat() / height.toFloat()
            val ratioMax = maxWidth.toFloat() / maxHeight.toFloat()

            if (ratioBitmap > ratioMax) {
                width = maxWidth
                height = (width / ratioBitmap).toInt()
            } else {
                height = maxHeight
                width = (height * ratioBitmap).toInt()
            }
        }
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

    /**
     * compressBitmap
     * 이미지 해상도 수정
     * @param bitmap
     * @param quality
     * @return
     */
    fun compressBitmap(bitmap: Bitmap, quality: Int): Bitmap {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    }

    /**
     * encodeToBase64
     * 이미지 base64 인코딩
     * @param bitmap
     * @param quality
     * @return
     */
    fun encodeToBase64(bitmap: Bitmap, quality: Int): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }

    /**
     * encodeToBase64UrlSafe
     * 이미지 base64 With +,-,/ 제거
     * @param bitmap
     * @param quality
     * @return
     */
    fun encodeToBase64UrlSafe(bitmap: Bitmap, quality: Int): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.URL_SAFE or Base64.NO_WRAP)
    }

    /**
     * encodeToBase64WebP
     * 이미지 base64 인코딩 WebP 타입으로 파싱
     * @param bitmap
     * @param quality
     * @return
     */
    fun encodeToBase64WebP(bitmap: Bitmap, quality: Int): String {
        val byteArrayOutputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.WEBP, quality, byteArrayOutputStream)
        val byteArray = byteArrayOutputStream.toByteArray()
        return Base64.encodeToString(byteArray, Base64.NO_WRAP)
    }
}