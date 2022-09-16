//
//  File.swift
//  WCStoreApp
//
//  Created by Hicham on 14/9/2022.
//

import Foundation
import WCStoreAppKmm

class Koin {
    static func get<T: AnyObject>(parameters: [Any]? = nil) -> T {
        return KoinKt.get(objCClass: T.self, parameters: parameters) as! T
    }
}
