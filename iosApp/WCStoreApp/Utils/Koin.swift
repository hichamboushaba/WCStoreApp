//
//  File.swift
//  WCStoreApp
//
//  Created by Hicham on 14/9/2022.
//

import Foundation
import WCStoreAppKmm

protocol A: AnyObject {}

class Koin {
    static func get<T: AnyObject>(parameters: [Any]? = nil) -> T {
        return KoinKt.get(objCClass: (T.self), parameters: parameters) as! T
    }
    
    static func getProtocol<T>(_ procolClass: Protocol) -> T {
        return KoinKt.get(objCProtocol: procolClass) as! T
    }
}
