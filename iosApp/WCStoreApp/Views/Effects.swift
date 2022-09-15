//
//  Toast.swift
//  WCStoreApp
//
//  Created by Hicham on 15/9/2022.
//

import Foundation
import SwiftUI
import WCStoreAppKmm
import KMPNativeCoroutinesCombine
import ExytePopupView

struct EffectsModifier: ViewModifier {
    let viewModel: BaseViewModel
    let unhandledEffect: (Effect) -> Void
    
    @State private var currentToast: Toast? = nil
    @State private var toastMessage = ""
    @State private var showActionToast = false
    
    func body(content: Content) -> some View {
        content
            .popup(
                isPresented: Binding(
                    get: { currentToast != nil },
                    set: { if (!$0) {currentToast = nil} }
                ),
                type: .floater(),
                position: .bottom,
                animation: .easeInOut(duration: 0.2),
                autohideIn: 2
            ) {
                switch(currentToast) {
                case let .Simple(message: message):
                    Text(message)
                        .bold()
                        .foregroundColor(.white)
                        .padding()
                        .frame(maxWidth: .infinity)
                        .background(Color.black)
                        .cornerRadius(8.0)
                        .shadow(radius: 4.0)
                        .padding()
                default:
                    let _ = print("TODO handle toast $currentToast")
                }
            }.onReceive(
                createPublisher(for: viewModel.effectsNative)
                    .assertNoFailure()
            ) { effect in
                switch(effect) {
                case let snackbar as ShowSnackbar:
                    currentToast = Toast.Simple(message: snackbar.message)
                case let actionSnackBar as ShowActionSnackbar:
                    currentToast = Toast.ActionToast(message: actionSnackBar.message, actionText: actionSnackBar.actionText, action: actionSnackBar.action)
                default: unhandledEffect(effect)
                }
            }
    }
}

private enum Toast {
    case Simple (message: String)
    case ActionToast (message: String, actionText: String, action: () -> Void)
}

extension View {
    func effects(viewModel: BaseViewModel, unhandledEffect: @escaping (Effect) -> Void = { _ in }) -> some View {
        modifier(EffectsModifier(viewModel: viewModel, unhandledEffect: unhandledEffect))
    }
}
