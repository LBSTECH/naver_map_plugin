//
//  JsonConversion.swift
//  naver_map_plugin
//
//  Created by Maximilian on 2020/08/20.
//

import Foundation
import NMapsMap
import Flutter

public func toLatLng(json: Any) -> NMGLatLng{
    let data = json as! Array<Double>
    return NMGLatLng(lat: data[0], lng: data[1])
}

public func toLatLngBounds(json: Any) -> NMGLatLngBounds{
    let data = json as! Array<Any>
    return NMGLatLngBounds(southWest: toLatLng(json: data[0]), northEast: toLatLng(json: data[1]))
}

public func toCameraPosition(json: Any) -> NMFCameraPosition{
    let data = json as! NSDictionary
    return NMFCameraPosition(toLatLng(json: data["target"]!),
                             zoom: data["zoom"] as! Double,
                             tilt: data["tilt"] as! Double,
                             heading: data["bearing"] as! Double)
}

public func toCameraUpdate(json: Any) -> NMFCameraUpdate{
    let data = json as! Array<Any>
    let type = data[0] as! String
    switch type {
    case "newCameraPosition":
        return NMFCameraUpdate(position: toCameraPosition(json: data[1]))
    case "scrollTo":
        return NMFCameraUpdate(scrollTo: toLatLng(json: data[1]))
    case "zoomIn":
        return NMFCameraUpdate.withZoomIn()
    case "zoomOut":
        return NMFCameraUpdate.withZoomOut()
    case "zoomTo":
        return NMFCameraUpdate(zoomTo: data[1] as! Double)
    case "fitBounds":
        let pt = data[2] as! Int
        return NMFCameraUpdate(fit: toLatLngBounds(json: data[1]), padding: CGFloat(pt))
    default:
        return NMFCameraUpdate()
    }
}

public func toColor(colorNumber: NSNumber) -> UIColor {
    let value = colorNumber.uint64Value
    let red = CGFloat(exactly: (value & 0xFF0000) >> 16)! / 255.0
    let green = CGFloat(exactly: (value & 0xFF00) >> 8)! / 255.0
    let blue = CGFloat(exactly: (value & 0xFF))! / 255.0
    let alpha = CGFloat(exactly: (value & 0xFF000000) >> 24)! / 255.0
    return UIColor(red: red, green: green, blue: blue, alpha: alpha)
}

public func ptFromPx(_ px: NSNumber) -> CGFloat {
    let resolutionFactor = UIScreen.main.nativeBounds.width / UIScreen.main.bounds.width
    return CGFloat(truncating: px) / resolutionFactor
}

public func pxFromPt(_ pt: CGFloat) -> Int {
    let resolutionFactor = UIScreen.main.nativeBounds.width / UIScreen.main.bounds.width
    return Int(pt * resolutionFactor)
}

public func toOverlayImage(assetPath: String, registrar: FlutterPluginRegistrar) -> NMFOverlayImage? {
    let assetName = registrar.lookupKey(forAsset: assetPath)
    if let image = UIImage(named: assetName) {
        return NMFOverlayImage(image: image)
    }
    
    return nil
}

// ============================= 객체를 json 으로 =================================


public func cameraPositionToJson(position: NMFCameraPosition) -> Dictionary<String, Any>{
    return [
        "tilt" : position.tilt,
        "target" : latlngToJson(latlng: position.target),
        "bearing" : position.heading,
        "zoom" : position.zoom
    ]
}

public func latlngToJson(latlng: NMGLatLng) -> Array<Double> {
    return [latlng.lat, latlng.lng]
}

public func latlngBoundToJson(bound: NMGLatLngBounds) -> Dictionary<String, Any>{
    return [
        "southwest" : latlngToJson(latlng: bound.southWest),
        "northeast" : latlngToJson(latlng: bound.northEast)
    ]
}

