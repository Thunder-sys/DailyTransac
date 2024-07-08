package com.example.dailytransac.Saksh

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dailytransac.R
import com.example.dailytransac.khush.Budgetscreen

class Mainpage : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_mainpage)

        var grap = findViewById<ImageButton>(R.id.imageButton)
        grap.setOnClickListener(){
            var intent =Intent(this,Budgetscreen::class.java)
            startActivity(intent)
        }



        var reco=findViewById<RecyclerView>(R.id.recy)
        var reco1=findViewById<RecyclerView>(R.id.recy1)
        var reco2=findViewById<RecyclerView>(R.id.recy2)

        var childitem:ArrayList<Model_reco> = ArrayList()
        childitem.add(Model_reco("76877","565476","65767"))
        childitem.add(Model_reco("76877","565476","65767"))
        childitem.add(Model_reco("76877","565476","65767"))
        childitem.add(Model_reco("76877","565476","65767"))
        childitem.add(Model_reco("76877","565476","65767"))
        childitem.add(Model_reco("76877","565476","65767"))
        childitem.add(Model_reco("76877","565476","65767"))
        childitem.add(Model_reco("76877","565476","65767"))

        var childitem1:ArrayList<Model_reco> = ArrayList()
        childitem1.add(Model_reco("76877","565476","65767"))
        childitem1.add(Model_reco("76877","565476","65767"))
        childitem1.add(Model_reco("76877","565476","65767"))
        childitem1.add(Model_reco("76877","565476","65767"))
        childitem1.add(Model_reco("76877","565476","65767"))
        childitem1.add(Model_reco("76877","565476","65767"))


        var listofdata:ArrayList<Model_mainpage> = ArrayList()
        var d1=Model_mainpage("04/05/2006","25000","20000","5000",childitem)
        var d2=Model_mainpage("04/05/2006","25000","20000","5000",childitem1)

        listofdata.add(d1)
        listofdata.add(d2)

        reco.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        var adaptt=Adapter_mainpage(listofdata)
        reco.adapter=adaptt


        var listofdetail:ArrayList<Model_daily> = ArrayList()
        var a1=Model_daily("04/05/2024","25000","20000","5000")
        var a2=Model_daily("04/05/2024","25000","20000","5000")
        var a3=Model_daily("04/05/2024","25800","23000","2000")
        var a4=Model_daily("04/05/2024","56551","6565","65485")

        listofdetail.add(a1)
        listofdetail.add(a2)
        listofdetail.add(a3)
        listofdetail.add(a4)

        reco1.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        var adapt=Adapter_daily(listofdetail)
        reco1.adapter=adapt

        var listofmonth:ArrayList<Model_monthly> = ArrayList()
        var b1=Model_monthly("20/05/2014","987451","54146","65874")
        var b2=Model_monthly("20/05/2014","987451","54146","65874")
        var b3=Model_monthly("20/05/2014","987451","54146","65874")
        var b4=Model_monthly("20/05/2014","987451","54146","65874")
        var b5=Model_monthly("20/05/2014","987451","54146","65874")

        listofmonth.add(b1)
        listofmonth.add(b2)
        listofmonth.add(b3)
        listofmonth.add(b4)
        listofmonth.add(b5)

        reco2.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        var adap=Adapter_monthly(listofmonth)
        reco2.adapter=adap





        }
    }
