import Flutter
import UIKit

public class SwiftWhatsappContactsPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "com.cuenca.plugin.whatsapp_contacts", binaryMessenger: registrar.messenger())
    let instance = SwiftWhatsappContactsPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result(FlutterMethodNotImplemented)
  }
}
