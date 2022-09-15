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
import SystemNotification

struct EffectsModifier: ViewModifier {
    let viewModel: BaseViewModel
    let unhandledEffect: (Effect) -> Void
    
    @StateObject private var notification = SystemNotificationContext()
    
    func body(content: Content) -> some View {
        content
            .systemNotification(notification)
            .onReceive(
                createPublisher(for: viewModel.effectsNative)
                    .assertNoFailure()
            ) { effect in
                switch(effect) {
                case let snackbar as ShowSnackbar:
                    showSimpleToast(message: snackbar.message)
                case let actionSnackBar as ShowActionSnackbar:
                    showActionToast(message: actionSnackBar.message, actionText: actionSnackBar.actionText, action: actionSnackBar.action)
                default: unhandledEffect(effect)
                }
            }
    }
    
    private func showSimpleToast(message: String) {
        notification.present(configuration: .init(backgroundColor: Color.black, cornerRadius: 8.0, edge: .bottom)) {
            Text(message)
                .bold()
                .foregroundColor(.white)
                .padding()
        }
    }
    
    private func showActionToast(message: String, actionText: String, action: @escaping () -> Void) {
        notification.present(configuration: .init(backgroundColor: Color.black, cornerRadius: 8.0, duration: 3, edge: .bottom)) {
            HStack {
                Text(message)
                    .bold()
                    .foregroundColor(.white)
                    .padding()
                Spacer()
                Button(action: {
                    action()
                    notification.dismiss()
                }) {
                    Text(actionText)
                }
            }.padding()
        }
    }
}

extension View {
    func effects(viewModel: BaseViewModel, unhandledEffect: @escaping (Effect) -> Void = { _ in }) -> some View {
        modifier(EffectsModifier(viewModel: viewModel, unhandledEffect: unhandledEffect))
    }
}
