package com.example.nurtour.ui.authorized.map

import android.content.Intent
import android.graphics.Color
import android.graphics.PointF
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.example.nurtour.R
import com.example.nurtour.common.constants.ARGConstants
import com.example.nurtour.common.constants.ARGConstants.ARG_PLACE_DETAIL
import com.example.nurtour.data.model.PlaceDTO
import com.example.nurtour.ui.authorized.placeDetail.PlaceDetailActivity
import com.yandex.mapkit.*
import com.yandex.mapkit.geometry.Point
import com.yandex.mapkit.geometry.Polyline
import com.yandex.mapkit.geometry.SubpolylineHelper
import com.yandex.mapkit.layers.ObjectEvent
import com.yandex.mapkit.map.*
import com.yandex.mapkit.transport.TransportFactory
import com.yandex.mapkit.transport.masstransit.*
import com.yandex.mapkit.transport.masstransit.SectionMetadata.SectionData
import com.yandex.mapkit.user_location.UserLocationLayer
import com.yandex.mapkit.user_location.UserLocationObjectListener
import com.yandex.mapkit.user_location.UserLocationView
import com.yandex.runtime.Error
import com.yandex.runtime.image.ImageProvider
import com.yandex.runtime.network.NetworkError
import com.yandex.runtime.network.RemoteError
import com.yandex.runtime.ui_view.ViewProvider
import kotlinx.android.synthetic.main.fragment_map.*
import org.koin.androidx.viewmodel.ext.android.viewModel


class MapFragment : Fragment(R.layout.fragment_map), MapObjectTapListener,
    UserLocationObjectListener, Session.RouteListener {

    private val viewModel by viewModel<MapViewModel>()

    private var mapkit: MapKit? =  null

    private var mapObjectCollection: MapObjectCollection? = null
    private var mapRouteObjectCollection: MapObjectCollection? = null
    private var userLocationLayer: UserLocationLayer? = null
    private var route: MasstransitRouter? = null

    private var placeLocation: Point? = null
    private var userLocationPoint: Point = Point(51.09711838158624,71.40667301289739)

    private var placeData: PlaceDTO? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapKitFactory.initialize(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //setUserLocation()
        mapkit = MapKitFactory.getInstance()
        moveCamera(userLocationPoint.latitude, userLocationPoint.longitude)
        observeViewModel()
        viewModel.onViewCreated()
        setOnclickListener()
    }

    override fun onStart() {
        super.onStart()
        mapview.onStart()
        MapKitFactory.getInstance().onStart()
    }

    override fun onStop() {
        super.onStop()
        mapview.onStop()
        MapKitFactory.getInstance().onStop()
    }

    private fun setOnclickListener() {
        closeIcon.setOnClickListener {
            placeInfoLayout.isVisible = false
        }
        walkView.setOnClickListener {
            walkView.isVisible = false
            clearView.isVisible = true
            buildRoute()
        }
        clearView.setOnClickListener {
            clearView.isVisible = false
            walkView.isVisible = true
            mapRouteObjectCollection?.clear()
        }
        infoView.setOnClickListener {
            val intent = Intent(activity, PlaceDetailActivity::class.java)
            intent.putExtra(ARG_PLACE_DETAIL, placeData)
            startActivity(intent)
        }
    }

    private fun observeViewModel() = with(viewModel) {
        place.observe(viewLifecycleOwner, Observer {
            addPlaceMarker(it)
        })
    }

    /**
     *  Настраиваем локацию пользователя
     */
    private fun setUserLocation() {
        userLocationLayer = mapkit?.createUserLocationLayer(mapview.mapWindow)
        userLocationLayer!!.isVisible = true
        userLocationLayer!!.isHeadingEnabled = true
        //userLocationLayer!!.setObjectListener(this)
    }

    /**
     *  Добавляем маркеры по координатам мест
     */
    private fun addPlaceMarker(it: List<PlaceDTO>) {
        mapObjectCollection = mapview.map.mapObjects.addCollection()

        it.forEach {
            val view = ViewProvider(View(requireContext()).apply {
                background = resources.getDrawable(R.drawable.ic_map)
                isClickable = true
                isFocusable = true
            })

            mapObjectCollection?.addPlacemark(Point(it.latitude, it.longitude))?.apply {
                setView(view)
                userData = it
                addTapListener(this@MapFragment)
            }

            val userLocationView = ViewProvider(View(requireContext()).apply {
                background = resources.getDrawable(R.drawable.ic_my_location)
                isFocusable = true
            })

            mapObjectCollection?.addPlacemark(Point(userLocationPoint.latitude, userLocationPoint.longitude))?.apply {
                setView(userLocationView)
            }
        }
    }

    /**
     *  Обрабатываем нажатия на маркер
     */
    override fun onMapObjectTap(p0: MapObject, p1: Point): Boolean {
        showPlaceInfo(p0)
        return true
    }

    /**
     *  Показываем дополнительную информацию
     */
    private fun showPlaceInfo(data: MapObject) {
        placeInfoLayout.isVisible = true
        placeData = data.userData as PlaceDTO

        if(placeData!=null) {
            moveCamera(placeData!!.latitude, placeData!!.longitude, 15.0f)
            placeLocation = Point(placeData!!.latitude, placeData!!.longitude)
            title.text = placeData!!.name
            description.text = placeData!!.description
        }
    }

    /**
     *  Меняем расположение камеры для карты
     */
    private fun moveCamera(
        lat: Double = 51.1282992,
        long: Double = 71.4304916,
        zoom: Float = 13.0f
    ) {
        mapview.map.move(
            CameraPosition(Point(lat, long), zoom, 0.0f, 0.0f),
            Animation(Animation.Type.SMOOTH, 1f),
            null
        )
    }

    /**
     *  Добавляем роут между двумя точками
     */
    private fun buildRoute() {
        mapRouteObjectCollection = mapview.map.mapObjects.addCollection()
        val options = MasstransitOptions(
            ArrayList(),
            ArrayList(),
            TimeOptions()
        )
        val points: MutableList<RequestPoint> = ArrayList()
        points.add(RequestPoint(placeLocation!!, RequestPointType.WAYPOINT, null))
        points.add(RequestPoint(userLocationPoint!!, RequestPointType.WAYPOINT, null))
        route = TransportFactory.getInstance().createMasstransitRouter()
        route!!.requestRoutes(points, options, this@MapFragment)
    }

    override fun onObjectAdded(userLocationView: UserLocationView) {
        userLocationLayer?.setAnchor(
            PointF((mapview.width * 0.5).toFloat(), (mapview.height * 0.5).toFloat()),
            PointF((mapview.width * 0.5).toFloat(), (mapview.height * 0.83).toFloat())
        )

        userLocationView.arrow.setIcon(
            ImageProvider.fromResource(
                context, R.drawable.ic_map
            )
        )

        val pinIcon: CompositeIcon = userLocationView.pin.useCompositeIcon()

        pinIcon.setIcon(
            "icon",
            ImageProvider.fromResource(context, R.drawable.ic_map),
            IconStyle().setAnchor(PointF(0f, 0f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(0f)
                .setScale(1f)
        )

        pinIcon.setIcon(
            "pin",
            ImageProvider.fromResource(context, R.drawable.ic_favorite),
            IconStyle().setAnchor(PointF(0.5f, 0.5f))
                .setRotationType(RotationType.ROTATE)
                .setZIndex(1f)
                .setScale(0.5f)
        )

        userLocationView.accuracyCircle.fillColor = Color.BLUE and -0x66000001
    }

    override fun onObjectRemoved(p0: UserLocationView) {
        // do nothing
    }

    override fun onObjectUpdated(p0: UserLocationView, p1: ObjectEvent) {
        //do nothing
    }

    override fun onMasstransitRoutes(routes: MutableList<Route>) {
        if (routes.size > 0) {
            for (section in routes[0].sections) {
                drawSection(
                    section.metadata.data,
                    SubpolylineHelper.subpolyline(
                        routes[0].geometry, section.geometry
                    )
                )
            }
        }
    }

    override fun onMasstransitRoutesError(error: Error) {
        var errorMessage = "Error"
        if (error is RemoteError) {
            errorMessage = "RemoteError"
        } else if (error is NetworkError) {
            errorMessage = "NetworkError"
        }

        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
    }

    private fun drawSection(
        data: SectionData,
        geometry: Polyline
    ) {
        // Draw a section polyline on a map
        // Set its color depending on the information which the section contains
        val polylineMapObject: PolylineMapObject = mapRouteObjectCollection?.addPolyline(geometry)!!
        // Masstransit route section defines exactly one on the following
        // 1. Wait until public transport unit arrives
        // 2. Walk
        // 3. Transfer to a nearby stop (typically transfer to a connected
        //    underground station)
        // 4. Ride on a public transport
        // Check the corresponding object for null to get to know which
        // kind of section it is
        if (data.transports != null) {
            // A ride on a public transport section contains information about
            // all known public transport lines which can be used to travel from
            // the start of the section to the end of the section without transfers
            // along a similar geometry
            for (transport in data.transports!!) {
                // Some public transport lines may have a color associated with them
                // Typically this is the case of underground lines
                if (transport.line.style != null) {
                    polylineMapObject.strokeColor = transport.line.style!!.color!! or -0x1000000
                    return
                }
            }
            // Let us draw bus lines in green and tramway lines in red
            // Draw any other public transport lines in blue
            val knownVehicleTypes: HashSet<String> = HashSet()
            knownVehicleTypes.add("bus")
            knownVehicleTypes.add("tramway")
            for (transport in data.transports!!) {
                val sectionVehicleType: String = getVehicleType(transport, knownVehicleTypes).orEmpty()
                if (sectionVehicleType == "bus") {
                    polylineMapObject.strokeColor = -0x1000000 // black
                    return
                } else if (sectionVehicleType == "tramway") {
                    polylineMapObject.strokeColor = -0x10000 // Red
                    return
                }
            }
            polylineMapObject.strokeColor = -0xffff01 // Blue
        } else {
            // This is not a public transport ride section
            // In this example let us draw it in black
            polylineMapObject.strokeColor = -0xff0100// Green
        }
    }

    private fun getVehicleType(transport: Transport, knownVehicleTypes: HashSet<String>): String? {
        for (type in transport.line.vehicleTypes) {
            if (knownVehicleTypes.contains(type)) {
                return type
            }
        }
        return null
    }
}